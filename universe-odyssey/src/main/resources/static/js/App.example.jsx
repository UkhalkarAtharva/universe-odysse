import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import Home from './pages/Home';
import Missions from './pages/Missions';
import DailyQuiz from './pages/quiz/DailyQuiz';
import Leaderboard from './pages/leaderboard/Leaderboard';
// PlanetDetails route removed to restore server-side chat workflow
// import PlanetDetails from './pages/PlanetDetails';

/**
 * Main App Component with React Router Configuration
 * 
 * Routes:
 * - "/" → Home page with planet cards
 * - "/missions" → NASA Missions page with search
 */
function App() {
  return (
    <Router>
      <Routes>
        {/* Home route - displays all planets as clickable cards */}
        <Route path="/" element={<Home />} />

        {/* Missions route - NASA missions with search */}
        <Route path="/missions" element={<Missions />} />

        {/* Planet Details route removed — planet cards now navigate to /chat?planet=Name */}

        <Route path="/quiz" element={<DailyQuiz />} />
        <Route path="/leaderboard" element={<Leaderboard />} />
        {/* Catch-all - redirect unknown routes to home */}
        <Route path="*" element={<Navigate to="/" replace />} />
      </Routes>
    </Router>
  );
}

export default App;
