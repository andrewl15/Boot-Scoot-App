import React from "react";
import styles from "./UserDanceCard.module.css";

export default function DanceCard({ dance, editedDances, handleToggleLearned }) {
    const isLearned = editedDances[dance.danceId]?.learned ?? dance.learned ?? false;

    return (
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

                <div className={styles.learnedWrapper}>
                    <div className={styles.checkboxWrapper}>
                        <input
                            type="checkbox"
                            id={`checkbox-${dance.danceId}`}
                            checked={isLearned}
                            onChange={() => handleToggleLearned(dance)}
                        />
                        <label htmlFor={`checkbox-${dance.danceId}`}>
                            <div className={styles.tickMark}></div>
                        </label>
                        <span className={styles.learnedLabel}>Learned</span>
                    </div>
                </div>
            </div>
        </div>
    );
}
