import { useContext, useEffect, useState } from "react";
import { UserContext } from "../../context/UserContext";
import DanceService from "../../services/DanceService";

export default function UserDanceListView() {
    const user = useContext(UserContext);
    const [dances, setDances] = useState([]);

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
            <div className="user-dance-list-view">
                <h1>{user ? `${user.username}'s Dance List` : 'Your Dance List'}</h1>
                {dances.length > 0 ? (
                    <ul>
                        {dances.map((dance, index) => (
                            <li key={index}>
                                <h2>{dance.danceName}</h2>
                                <p>Song: {dance.songName}</p>
                                <p>Artist: {dance.artistName}</p>
                                <p>Wall: {dance.walls}</p>
                                <p>Count: {dance.count}</p>
                                <p>Level: {dance.level}</p>
                            </li>
                        ))}
                    </ul>
                ) : (
                    <p>No dances found.</p>
                )}
            </div>
        </>
    );
}