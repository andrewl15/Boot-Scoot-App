import { useContext, useEffect, useState, useRef } from "react";
import { UserContext } from "../../context/UserContext";
import DanceService from "../../services/DanceService";
import UserDanceCard from "../../components/UserDanceCard/UserDanceCard";
import styles from "./UserDanceListView.module.css";
import { TransitionGroup, CSSTransition } from "react-transition-group";
import { FaSave } from "react-icons/fa";
import { IoFilterSharp } from "react-icons/io5";

export default function UserDanceListView() {
    const user = useContext(UserContext);
    const [dances, setDances] = useState([]);
    const [searchTerm, setSearchTerm] = useState("");
    const [filteredDances, setFilteredDances] = useState([]);
    const [editedDances, setEditedDances] = useState({});
    const debounceTimeout = useRef(null);
    const [filterOpen, setFilterOpen] = useState(false);

    function openFilterWindow() {
        setFilterOpen(!filterOpen);
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
        if (user) {
            DanceService.getDancesByUserId(user.id)
                .then(response => {
                    // Sort learned first
                    const sorted = [...response.data].sort((a, b) => {
                        return (b.learned === true) - (a.learned === true);
                    });

                    setDances(sorted);
                    setFilteredDances(sorted); // initially show all
                })
                .catch(error => {
                    console.error("Error fetching uUserDanceCardser dances:", error);
                });
        }
    }, [user]);


    // debounce search effect
    useEffect(() => {
        if (debounceTimeout.current) {
            clearTimeout(debounceTimeout.current);
        }

        debounceTimeout.current = setTimeout(() => {
            if (!searchTerm.trim()) {
                setFilteredDances(dances);
                return;
            }
            const lowerSearch = searchTerm.toLowerCase();

            const filtered = dances.filter(dance =>
                dance.songName.toLowerCase().includes(lowerSearch) ||
                dance.danceName.toLowerCase().includes(lowerSearch)
            );

            setFilteredDances(filtered);
        }, 300);

        return () => clearTimeout(debounceTimeout.current);
    }, [searchTerm, dances]);

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
                    <button className={styles.searchButton} onClick={openFilterWindow} aria-label="Search">
                        <IoFilterSharp size={30} />
                    </button>

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
            {filterOpen &&
                <div className={styles.filterWindow}>
                    hi
                </div>
            }
        </>
    );
}
