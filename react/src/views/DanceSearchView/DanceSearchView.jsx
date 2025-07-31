import { useContext, useEffect, useState } from "react";
import { TransitionGroup, CSSTransition } from "react-transition-group";
import DanceService from "../../services/DanceService";
import { UserContext } from "../../context/UserContext";
import styles from "./DanceSearchView.module.css";
import { MdOutlineAddBox } from "react-icons/md";
import { MdArrowForward } from "react-icons/md";



export default function DanceSearchView() {
    const user = useContext(UserContext);
    const [searchTerm, setSearchTerm] = useState("");
    const [searchResults, setSearchResults] = useState([]);
    const [dances, setDances] = useState([]);
    const [notification, setNotification] = useState("");
    const [showNotification, setShowNotification] = useState(false);
    const [notificationType, setNotificationType] = useState("success"); // "success" or "error"


    function notify(message, type = "success") {
        setNotification(message);
        setNotificationType(type);
        setShowNotification(true);

        setTimeout(() => {
            setShowNotification(false);
        }, 3000);
    }

    function handleAddDance(link) {
        const danceId = extractCopperknobId(link);

        if (checkIfDanceExists(danceId)) {
            notify("This dance is on your list!", "error");
            return;
        }

        DanceService.addDance(link)
            .then(res => {
                notify("Dance added to your list!", "success");
            })
            .catch(err => {
                alert("Something went wrong while saving the dance.");
            });
    }



    function getSearchResults() {
        if (!searchTerm.trim()) {
            return;
        }
        DanceService.searchDances(searchTerm)
            .then(response => {
                setSearchResults(response.data);
            })
            .catch(error => {
                console.error("Error fetching search results:", error);
            });
    }

    function checkIfDanceExists(id) {
        const idNum = Number(id);
        return dances.some(dance => dance.danceId === idNum);
    }

    function extractCopperknobId(url) {
        const match = url.match(/\/stepsheets\/(\d+)\//);
        return match ? match[1] : null;
    }
    useEffect(() => {
        if (user) {
            DanceService.getDancesByUserId(user.id)
                .then(response => {
                    setDances(response.data);
                })
                .catch(error => {
                    console.error("Error fetching user dances:", error);
                });
        }
    }, [user]);
    return (
        <>
            <div className={styles.container}>
                <div className={styles.searchBar}>
                    <input
                        type="text"
                        className={styles.input}
                        placeholder="Search for a dance..."
                        onChange={e => setSearchTerm(e.target.value)}
                    />
                    <button className={styles.searchButton} onClick={getSearchResults} aria-label="Search">
                        <MdArrowForward size={30} />
                    </button>
                </div>

                <TransitionGroup component={null}>
                    {searchResults.map((dance, index) => (
                        <CSSTransition
                            key={index}
                            timeout={300}
                            classNames={{
                                enter: styles.resultEnter,
                                enterActive: styles.resultEnterActive,
                                exit: styles.resultExit,
                                exitActive: styles.resultExitActive
                            }}
                        >
                            <div key={index} className={styles.danceResult}>
                                <div className={styles.resultHeader}>
                                    <h2 className={styles.danceTitle}>{dance.title}</h2>
                                    <span className={styles.badge}>{dance.level}</span>
                                </div>

                                <div className={styles.metaRow}>
                                    <p><strong>Artist:</strong> {dance.artist}</p>
                                    <p><strong>Wall:</strong> {dance.wall}</p>
                                    <p><strong>Count:</strong> {dance.count}</p>
                                </div>

                                <div className={styles.actions}>
                                    <a className={styles.link} href={dance.url} target="_blank" rel="noopener noreferrer">
                                        View on Copperknob
                                    </a>
                                    <button className={styles.addButton} onClick={() => handleAddDance(dance.url)}>
                                        <MdOutlineAddBox size={20} />
                                        Add to My List
                                    </button>
                                </div>
                            </div>

                        </CSSTransition>
                    ))}
                </TransitionGroup>

            </div>
            {showNotification && (
                <div
                    className={`${styles.notification} ${styles.slideUp} ${notificationType === "error" ? styles.error : styles.success
                        }`}
                >
                    {notification}
                </div>
            )}




        </>

    );
}