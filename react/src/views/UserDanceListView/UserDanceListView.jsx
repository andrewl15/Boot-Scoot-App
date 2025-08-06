import { useContext, useEffect, useState, useRef } from "react";
import { UserContext } from "../../context/UserContext";
import DanceService from "../../services/DanceService";
import styles from "./UserDanceListView.module.css";
import { TransitionGroup, CSSTransition } from "react-transition-group";


export default function UserDanceListView() {
    const user = useContext(UserContext);
    const [dances, setDances] = useState([]);
    const [searchTerm, setSearchTerm] = useState("");
    const [filteredDances, setFilteredDances] = useState([]);
    const debounceTimeout = useRef(null);
    const [editedDances, setEditedDances] = useState({});

    function handleToggleLearned(danceToUpdate) {
        const currentValue = editedDances[danceToUpdate.danceId]?.isLearned ?? danceToUpdate.isLearned;

        setEditedDances(prev => ({
            ...prev,
            [danceToUpdate.danceId]: {
                ...danceToUpdate,
                isLearned: !currentValue
            }
        }));
        console.log("Toggled learned state for dance:", danceToUpdate.danceId, "New state:", !currentValue);
    }

    function handleSaveChanges() {
    }


    useEffect(() => {
        if (user) {
            DanceService.getDancesByUserId(user.id)
                .then(response => {
                    setDances(response.data);
                    setFilteredDances(response.data); // initially show all
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
        }, 300); // 300ms debounce delay

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

                </div>
                {filteredDances.length > 0 ? (
                    <TransitionGroup component={null}>
                        {filteredDances.map((dance) => (
                            <CSSTransition
                                key={dance.danceId ?? dance.id ?? (dance.danceName + dance.songName)}
                                timeout={300}
                                classNames={{
                                    enter: styles.danceEnter,
                                    enterActive: styles.danceEnterActive,
                                    exit: styles.danceExit,
                                    exitActive: styles.danceExitActive,
                                }}
                            >
                                <div className={styles.danceCard}>
                                    <div className={styles.cardHeader}>
                                        <div className={styles.danceDetails}>
                                            <h2 className={styles.danceTitle}>{dance.danceName}</h2>
                                            <p><strong>Song:</strong> {dance.songName}</p>
                                            <p><strong>Artist:</strong> {dance.artistName}</p>
                                            <div className={styles.wallCountRow}>
                                                <p><strong>Wall:</strong> {dance.walls}</p>
                                                <p><strong>Count:</strong> {dance.count}</p>
                                            </div>
                                            <p><strong>Level:</strong> {dance.level}</p>
                                            <div className={styles.actions}>
                                                <a className={styles.link} href={dance.copperknobLink} target="_blank" rel="noopener noreferrer">
                                                    Copperknob
                                                </a>
                                            </div>

                                        </div>

                                        <div className={styles.buttonGroup}>
                                            {dance.demoUrl && (
                                                <a
                                                    href={dance.demoUrl}
                                                    className={styles.youtubeButton}
                                                    target="_blank"
                                                    rel="noopener noreferrer"
                                                >
                                                    <svg
                                                        className={styles.youtubeIcon}
                                                        xmlns="http://www.w3.org/2000/svg"
                                                        viewBox="0 0 576 512"
                                                        width="20"
                                                        height="20"
                                                    >
                                                        <path
                                                            fill="red"
                                                            d="M549.655 124.083c-6.281-23.65-24.812-42.176-48.46-48.454C458.539 64 288 64 288 64s-170.54 0-213.196 11.63c-23.648 6.278-42.18 24.804-48.46 48.454C16.007 167.23 16.007 256 16.007 256s0 88.77 10.337 131.917c6.281 23.65 24.812 42.176 48.46 48.454C117.46 448 288 448 288 448s170.539 0 213.195-11.629c23.648-6.278 42.18-24.804 48.46-48.454C559.993 344.77 559.993 256 559.993 256s0-88.77-10.338-131.917zM232 336V176l142.857 80L232 336z"
                                                        />
                                                    </svg>
                                                    Demo
                                                </a>
                                            )}
                                            {dance.tutorialUrl && (
                                                <a
                                                    href={dance.tutorialUrl}
                                                    className={styles.youtubeButton}
                                                    target="_blank"
                                                    rel="noopener noreferrer"
                                                >
                                                    <svg
                                                        className={styles.youtubeIcon}
                                                        xmlns="http://www.w3.org/2000/svg"
                                                        viewBox="0 0 576 512"
                                                        width="20"
                                                        height="20"
                                                    >
                                                        <path
                                                            fill="red"
                                                            d="M549.655 124.083c-6.281-23.65-24.812-42.176-48.46-48.454C458.539 64 288 64 288 64s-170.54 0-213.196 11.63c-23.648 6.278-42.18 24.804-48.46 48.454C16.007 167.23 16.007 256 16.007 256s0 88.77 10.337 131.917c6.281 23.65 24.812 42.176 48.46 48.454C117.46 448 288 448 288 448s170.539 0 213.195-11.629c23.648-6.278 42.18-24.804 48.46-48.454C559.993 344.77 559.993 256 559.993 256s0-88.77-10.338-131.917zM232 336V176l142.857 80L232 336z"
                                                        />
                                                    </svg>
                                                    Tutorial
                                                </a>
                                            )}

                                        </div>
                                        <label className={styles.toggleContainer}>
                                            <span>Learned</span>
                                            <input
                                                type="checkbox"
                                                className={styles.toggleInput}
                                                checked={
                                                    editedDances[dance.danceId]?.isLearned ??
                                                    (dance.isLearned ?? false)
                                                }
                                                onChange={() => handleToggleLearned(dance)}
                                            />
                                        </label>
                                    </div>
                                </div>
                            </CSSTransition>
                        ))}
                    </TransitionGroup>
                ) : (
                    <div className={styles.noDances}>
                        <p>No dances found</p>
                    </div>
                )}
            </div>
            {Object.keys(editedDances).length > 0 && (
                <div className={styles.saveChangesPopup}>
                    <p>You have unsaved changes.</p>
                    <button onClick={handleSaveChanges}>Save Changes</button>
                </div>
            )}
        </>
    );
}
