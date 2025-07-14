import { useState } from "react";
import DanceService from "../../services/DanceService";

export default function DanceSearchView() {
    const [searchTerm, setSearchTerm] = useState("");
    const [searchResults, setSearchResults] = useState([]);

    function getSearchResults() {
        DanceService.searchDances(searchTerm)
            .then(response => {
                setSearchResults(response.data);
            })
            .catch(error => {
                console.error("Error fetching search results:", error);
            });
    }
    return (
        <div className="dance-search-view">
            <h1>Dance Search</h1>
            <p>Search for dances here!</p>
            <input type="text" placeholder="Search for a dance..." onChange={e => setSearchTerm(e.target.value)} />
            <button onClick={getSearchResults}>Search</button>
            {
                searchResults.map(
                    (dance, index) => (
                        <div key={index} className="dance-result">
                            <h1>{dance.title}</h1>
                            <p>artist: {dance.artist}</p>
                            <p>wall: {dance.wall}</p>
                            <p>count: {dance.count}</p>
                            <p>level: {dance.level}</p>
                            <a href={dance.url} target="_blank" rel="noopener noreferrer">Visit Copperknob Page</a>
                        </div>

                    ))
            }
        </div>
    );
}