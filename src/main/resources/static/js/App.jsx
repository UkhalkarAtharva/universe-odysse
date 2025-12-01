// PlanetDetails route removed to restore server-side chat workflow
// import PlanetDetails from './pages/PlanetDetails';

/**
 * Main App Component with React Router Configuration
 * 
 * Routes:
 * - "/" → Home page with planet cards
 * - "/missions" → NASA Missions page with search
 */




import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import Home from './pages/Home';
import Missions from './pages/Missions';
import DailyQuiz from './pages/quiz/DailyQuiz';
import Leaderboard from './pages/leaderboard/Leaderboard';
import News from './pages/news/News';
import NewsDetail from './pages/news/NewsDetail';
// PlanetDetails route removed to restore server-side chat workflow
// import PlanetDetails from './pages/PlanetDetails';

function App() {
	return (
		<Router>
			<Routes>
				<Route path="/" element={<Home />} />
				<Route path="/missions" element={<Missions />} />
				<Route path="/quiz" element={<DailyQuiz />} />
				<Route path="/leaderboard" element={<Leaderboard />} />
				<Route path="/news" element={<News />} />
				<Route path="/news/:id" element={<NewsDetail />} />
				<Route path="/blogs" element={<BlogList />} />
				<Route path="/blogs/new" element={<BlogEditor />} />
				<Route path="/blogs/:id" element={<BlogDetail />} />
				<Route path="/blogs/:id/edit" element={<BlogEditor />} />
				<Route path="*" element={<Navigate to="/" replace />} />
			</Routes>
		</Router>
	);
}

export default App;
