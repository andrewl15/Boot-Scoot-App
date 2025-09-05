import { useContext, useEffect, useState } from "react";
import { TransitionGroup, CSSTransition } from "react-transition-group";
import DanceService from "../../services/DanceService";
import { UserContext } from "../../context/UserContext";
import DanceSearchCard from "../../components/DanceSearchView/DanceSearchCard";
import styles from "./DanceSearchView.module.css";
import { MdArrowForward } from "react-icons/md";
import { FaSearch } from "react-icons/fa";

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
            .then(() => {
                notify("Dance added to your list!", "success");
            })
            .catch(() => {
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
                        placeholder="Search..."
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
                            <DanceSearchCard
                                dance={dance}
                                handleAddDance={handleAddDance}
                            />
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