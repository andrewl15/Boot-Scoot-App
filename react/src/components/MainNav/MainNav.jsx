import { useContext, useState, useEffect } from 'react';
import { NavLink, Link } from 'react-router-dom';
import { UserContext } from '../../context/UserContext';
import styles from './MainNav.module.css';

export default function MainNav() {
  const user = useContext(UserContext);
  const [menuOpen, setMenuOpen] = useState(false);

  const toggleMenu = () => setMenuOpen(prev => !prev);
  const closeMenu = () => setMenuOpen(false);

  // Prevent body scroll when menu is open
  useEffect(() => {
    document.body.style.overflow = menuOpen ? 'hidden' : 'auto';
  }, [menuOpen]);

  return (
    <nav className={styles.mainNav}>
      <div className={styles.navHeader}>
        <h1 ><Link className={styles.logo} to={"/"}>Boot Scoot</Link> </h1>
        <div
          className={`${styles.burger} ${menuOpen ? styles.open : ''}`}
          onClick={toggleMenu}
        >
          <span className={styles.line}></span>
          <span className={styles.line}></span>
          <span className={styles.line}></span>
        </div>
      </div>

      <div className={`${styles.overlay} ${menuOpen ? styles.open : ''}`}>
        <NavLink
          to="/"
          onClick={closeMenu}
          className={({ isActive }) =>
            isActive ? `${styles.navItem} ${styles.active}` : styles.navItem
          }
        >Home
        </NavLink>
        {user ? (
          <>
            <NavLink to="/userProfile" className={({ isActive }) =>
              isActive ? `${styles.navItem} ${styles.active}` : styles.navItem}
              onClick={closeMenu}>Profile
            </NavLink>
            <NavLink to="/danceSearch" className={({ isActive }) =>
              isActive ? `${styles.navItem} ${styles.active}` : styles.navItem}
              onClick={closeMenu}>Search Dances
            </NavLink>
            <NavLink to={`/userDanceList/${user.id}`} className={({ isActive }) =>
              isActive ? `${styles.navItem} ${styles.active}` : styles.navItem}
              onClick={closeMenu}>My Dances
            </NavLink>
            <Link to="/logout" className={styles.navItem} onClick={closeMenu}>Logout</Link>
          </>
        ) : (
          <NavLink to="/login" className={({ isActive }) =>
            isActive ? `${styles.navItem} ${styles.active}` : styles.navItem}
            onClick={closeMenu}>Login
          </NavLink>
        )}
      </div>
    </nav>
  );
}
