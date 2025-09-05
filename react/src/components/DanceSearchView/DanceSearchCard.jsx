import React from "react";
import styles from "./DanceSearchCard.module.css";

export default function DanceSearchResult({ dance, handleAddDance }) {
    return (
        <div className={styles.danceResult}>
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
                    Copperknob Page
                </a>
                <button className={styles.addButton} onClick={() => handleAddDance(dance.url)}>
                    Add to My List
                </button>
            </div>
        </div>
    );
}
