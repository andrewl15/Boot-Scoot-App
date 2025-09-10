import styles from "./HomeView.module.css";
import { useContext } from "react";
import { UserContext } from "../../context/UserContext";
import { FaSearch, FaList, FaUser, FaArrowRight } from "react-icons/fa";
import { LuUserRound } from "react-icons/lu";


export default function HomeView() {
  const user = useContext(UserContext);
  const newUserSteps = [
    {
      icon: <FaUser size={40} color="#cd8032" />,
      title: "Create Your Account",
      description: "Sign up or log in to start tracking your dances and favorites.",
    },
    {
      icon: <FaArrowRight size={40} color="#cd8032" />,
      title: "Get Started",
      description: "Login or sign up to begin your line dance journey today!",
    },
  ];
  const userSteps = [
    {
      icon: <FaSearch size={40} color="#cd8032" />,
      title: "Search Dances",
      description: "Use the Dance Search page to find line dances from Copperknobs easily.",
    },
    {
      icon: <FaList size={40} color="#cd8032" />,
      title: "Manage Your List",
      description: "Save dances to your list, mark them as learned, and track your progress.",
    },
    {
      icon: <LuUserRound size={40} color="#cd8032" />,
      title: "Manage Your Profile",
      description: "Update your profile information and preferences in the User Profile section.",
    },
  ];

  return (
    <>
      {user &&
        <>
          <div className={styles.hero}>
            <h1 className={styles.heading}>
              Welcome back <span className={styles.brand}>{user.username.charAt(0).toUpperCase() + user.username.substring(1).toLowerCase()}</span>
            </h1>
            <p className={styles.subheading}>
              Your ultimate line dance manager
            </p>
          </div>
          <div className={styles.container}>
            <div className={styles.grid}>
              {userSteps.map((step, index) => (
                <div key={index} className={styles.card}>
                  <div className={styles.icon}>{step.icon}</div>
                  <h3 className={styles.cardTitle}>{step.title}</h3>
                  <p className={styles.cardDescription}>{step.description}</p>
                </div>
              ))}
            </div>
          </div>
        </>
      }
      {!user &&
        <>
          <div className={styles.hero}>
            <h1 className={styles.heading}>
              Welcome to <span className={styles.brand}>Boot Scoot</span>
            </h1>
            <p className={styles.subheading}>
              Your ultimate line dance manager
            </p>
          </div>
          <div className={styles.container}>
            <div className={styles.grid}>
              {newUserSteps.map((step, index) => (
                <div key={index} className={styles.card}>
                  <div className={styles.icon}>{step.icon}</div>
                  <h3 className={styles.cardTitle}>{step.title}</h3>
                  <p className={styles.cardDescription}>{step.description}</p>
                </div>
              ))}
            </div>
          </div>
        </>
      }
    </>

  );
}
