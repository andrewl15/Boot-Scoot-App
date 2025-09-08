import { useContext, useEffect, useState, useRef } from "react";
import { UserContext } from "../../context/UserContext";
import DanceService from "../../services/DanceService";
import UserDanceCard from "../../components/UserDanceComponents/UserDanceCard";
import styles from "./UserDanceListView.module.css";
import { TransitionGroup, CSSTransition } from "react-transition-group";
import { IoFilterSharp } from "react-icons/io5";


export default function UserDanceListView() {
    const user = useContext(UserContext);
    const [dances, setDances] = useState([]);
    const [searchTerm, setSearchTerm] = useState("");
    const [filteredDances, setFilteredDances] = useState([]);
    const [editedDances, setEditedDances] = useState({});
    const debounceTimeout = useRef(null);
    const [filterOpen, setFilterOpen] = useState(false);
    const [windowPosition, setWindowPosition] = useState({ top: 0, left: 0 });
    const [closing, setClosing] = useState(false);
    const buttonRef = useRef(null);
    const windowRef = useRef(null);
    const [selectedFilter, setSelectedFilter] = useState(null);

    function selectFilter(option) {
        setSelectedFilter(prev => (prev === option ? null : option)); // toggle
        startClosing();            // close the filter window with animation
    }


    function openFilterWindow() {
        if (buttonRef.current) {
            const rect = buttonRef.current.getBoundingClientRect();
            setWindowPosition({
                top: rect.bottom + window.scrollY + 8,
                left: rect.right + window.scrollX,
            });
        }
        if (!filterOpen) {
            setFilterOpen(true);
            setClosing(false);
        } else {
            startClosing();
        }
    }

    function startClosing() {
        setClosing(true);
        setTimeout(() => {
            setFilterOpen(false);
            setClosing(false);
        }, 300); // match CSS animation duration
    }

    function handleToggleLearned(danceToUpdate) {
        const currentValue =
            editedDances[danceToUpdate.danceId]?.learned ?? danceToUpdate.learned;

        const newValue = !currentValue;

        // Optimistic update
        setEditedDances(prev => ({
            ...prev,
            [danceToUpdate.danceId]: {
                ...danceToUpdate,
                learned: newValue
            }
        }));

        DanceService.updateLearnedStatus(danceToUpdate.danceId, newValue)
            .catch(error => {
                console.error("Error updating dance:", error);

                // Rollback
                setEditedDances(prev => ({
                    ...prev,
                    [danceToUpdate.danceId]: {
                        ...danceToUpdate,
                        learned: currentValue
                    }
                }));
            });
    }

    useEffect(() => {
        function handleClickOutside(e) {
            if (
                windowRef.current &&
                !windowRef.current.contains(e.target) &&
                !buttonRef.current.contains(e.target)
            ) {
                startClosing();
            }
        }

        function handleScroll() {
            startClosing();
        }

        if (filterOpen) {
            document.addEventListener("mousedown", handleClickOutside);
            window.addEventListener("scroll", handleScroll, true);
        } else {
            document.removeEventListener("mousedown", handleClickOutside);
            window.removeEventListener("scroll", handleScroll, true);
        }

        return () => {
            document.removeEventListener("mousedown", handleClickOutside);
            window.removeEventListener("scroll", handleScroll, true);
        };
    }, [filterOpen]);


    useEffect(() => {
        if (user) {
            DanceService.getDancesByUserId(user.id)
                .then(response => {
                    // Normalize learned to boolean
                    const normalized = response.data.map(dance => ({
                        ...dance,
                        learned: dance.learned === true || dance.learned === "true"
                    }));

                    // Sort Not Learned first
                    const sorted = [...normalized].sort((a, b) => {
                        return (a.learned === true) - (b.learned === true);
                    });

                    setDances(sorted);
                    setFilteredDances(sorted); // show everything initially
                })
                .catch(error => {
                    console.error("Error fetching user dances:", error);
                });
        }
    }, [user]);

    // debounce search effect
    useEffect(() => {
        if (debounceTimeout.current) {
            clearTimeout(debounceTimeout.current);
        }

        debounceTimeout.current = setTimeout(() => {
            // Merge original dances with edited values
            let mergedDances = dances.map(dance => {
                if (editedDances[dance.danceId]) {
                    return { ...dance, ...editedDances[dance.danceId] };
                }
                return dance;
            });

            // Sort: Not Learned first
            mergedDances = mergedDances.sort((a, b) => {
                return (a.learned === true) - (b.learned === true);
            });

            // Apply search
            if (searchTerm.trim()) {
                const lowerSearch = searchTerm.toLowerCase();
                mergedDances = mergedDances.filter(
                    dance =>
                        dance.songName.toLowerCase().includes(lowerSearch) ||
                        dance.danceName.toLowerCase().includes(lowerSearch)
                );
            }

            // Apply Learned/Not Learned filter
            if (selectedFilter === "Learned") {
                mergedDances = mergedDances.filter(dance => dance.learned === true);
            } else if (selectedFilter === "Not Learned") {
                mergedDances = mergedDances.filter(dance => dance.learned === false);
            }

            setFilteredDances(mergedDances);
        }, 200);

        return () => clearTimeout(debounceTimeout.current);
    }, [searchTerm, dances, selectedFilter, editedDances]);


    return (
        <>
            <div className={styles.container}>
                <div className={styles.searchBar}>
                    <input
                        type="text"
                        className={styles.input}
                        placeholder="Search your list..."
                        value={searchTerm}
                        onChange={e => setSearchTerm(e.target.value)}
                        aria-label="Search dances by song or dance name"
                    />
                    <button
                        className={styles.filterButton}
                        onClick={openFilterWindow}
                        ref={buttonRef}
                        aria-label="Filter"
                    >
                        <IoFilterSharp size={30} />
                    </button>
                    {filterOpen && (
                        <div
                            ref={windowRef}
                            className={`${styles.filterWindow} ${closing ? styles.closing : ""}`}
                            style={{
                                top: windowPosition.top + 8,
                                left: windowPosition.left - 220,
                            }}
                        >
                            <h4 className={styles.filterTitle}>Filters</h4>
                            <div className={styles.filterOptions}>
                                {["Learned", "Not Learned"].map(option => (
                                    <button
                                        key={option}
                                        onClick={() => selectFilter(option)}
                                        className={`${styles.filterOption} ${selectedFilter === option ? styles.active : ""
                                            }`}
                                    >
                                        {option}
                                    </button>
                                ))}
                            </div>
                        </div>
                    )}

                </div>
                {filteredDances.length > 0 ? (
                    <TransitionGroup component={null}>
                        {filteredDances.map((dance) => (
                            <CSSTransition
                                key={dance.danceId}
                                timeout={300}
                                classNames={{
                                    enter: styles.danceEnter,
                                    enterActive: styles.danceEnterActive,
                                    exit: styles.danceExit,
                                    exitActive: styles.danceExitActive,
                                }}
                            >
                                <UserDanceCard
                                    dance={dance}
                                    editedDances={editedDances}
                                    handleToggleLearned={handleToggleLearned}
                                />
                            </CSSTransition>
                        ))}
                    </TransitionGroup>
                ) : (
                    <div className={styles.noDances}>
                        <p>No dances found</p>
                    </div>
                )}
            </div>
        </>
    );
}
