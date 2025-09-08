import { TransitionGroup, CSSTransition } from "react-transition-group";
import UserDanceCard from "./UserDanceCard";

export default function DanceList({ dances, handleToggleLearned }) {
  return (
    <TransitionGroup className="danceList">
      {dances.map(d => (
        <CSSTransition
          key={d.danceId}
          timeout={400}           // animation duration
          classNames="dance"      // CSS classes like .dance-enter, .dance-exit
        >
          <UserDanceCard dance={d} onToggle={() => handleToggleLearned(d.danceId)} />
        </CSSTransition>
      ))}
    </TransitionGroup>
  );
}
