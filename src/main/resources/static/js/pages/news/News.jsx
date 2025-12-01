import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import './News.css';

const News = () => {
    const navigate = useNavigate();
    const [news, setNews] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [currentPage, setCurrentPage] = useState(0);
    const [totalPages, setTotalPages] = useState(0);
    const [selectedSource, setSelectedSource] = useState('');
    const [selectedCategory, setSelectedCategory] = useState('');
    const [sources, setSources] = useState([]);
    const [categories, setCategories] = useState([]);

    const pageSize = 20;

    useEffect(() => {
        fetchSources();
        fetchCategories();
    }, []);

    useEffect(() => {
        fetchNews();
    }, [currentPage, selectedSource, selectedCategory]);

    const fetchSources = async () => {
        try {
            const response = await fetch('/api/news/sources');
            const data = await response.json();
            setSources(data);
        } catch (err) {
            console.error('Error fetching sources:', err);
        }
    };

    const fetchCategories = async () => {
        try {
            const response = await fetch('/api/news/categories');
            const data = await response.json();
            setCategories(data);
        } catch (err) {
            console.error('Error fetching categories:', err);
        }
    };

    const fetchNews = async () => {
        setLoading(true);
        setError(null);

        try {
            let url = `/api/news?page=${currentPage}&size=${pageSize}`;
            if (selectedSource) url += `&source=${selectedSource}`;
            if (selectedCategory) url += `&category=${selectedCategory}`;

            const response = await fetch(url);
            if (!response.ok) throw new Error('Failed to fetch news');

            const data = await response.json();
            setNews(data.content);
            setTotalPages(data.totalPages);
            setLoading(false);
        } catch (err) {
            setError(err.message);
            setLoading(false);
        }
    };

    const handleSourceChange = (e) => {
        setSelectedSource(e.target.value);
        setCurrentPage(0);
    };

    const handleCategoryChange = (e) => {
        setSelectedCategory(e.target.value);
        setCurrentPage(0);
    };

    const handlePageChange = (newPage) => {
        setCurrentPage(newPage);
        window.scrollTo({ top: 0, behavior: 'smooth' });
    };

    const formatDate = (dateString) => {
        const date = new Date(dateString);
        return date.toLocaleDateString('en-US', { year: 'numeric', month: 'long', day: 'numeric' });
    };

    const getSourceBadgeClass = (source) => {
        const badges = {
            'NASA': 'badge-nasa',
            'ESA': 'badge-esa',
            'ISRO': 'badge-isro',
            'Spaceflight': 'badge-spaceflight'
        };
        return badges[source] || 'badge-default';
    };

    return (
        <div className="news-page">
            {/* Navigation */}
            <nav className="glass-panel sticky top-0 z-50">
                <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
                    <div className="flex items-center justify-between h-16">
                        <div className="flex items-center">
                            <a href="/" className="text-2xl font-bold bg-clip-text text-transparent bg-gradient-to-r from-blue-400 to-purple-500">
                                Universe Odyssey
                            </a>
                        </div>
                        <div className="hidden md:block">
                            <div className="ml-10 flex items-baseline space-x-4">
                                <a href="/" className="text-gray-300 hover:text-white px-3 py-2 rounded-md text-sm font-medium transition-colors">Home</a>
                                <a href="/quiz" className="text-gray-300 hover:text-white px-3 py-2 rounded-md text-sm font-medium transition-colors">Daily Quiz</a>
                                <a href="/leaderboard" className="text-gray-300 hover:text-white px-3 py-2 rounded-md text-sm font-medium transition-colors">Leaderboard</a>
                                <a href="/missions" className="text-gray-300 hover:text-white px-3 py-2 rounded-md text-sm font-medium transition-colors">Missions</a>
                                <a href="/news" className="text-blue-400 bg-blue-900/20 px-3 py-2 rounded-md text-sm font-medium">News</a>
                            </div>
                        </div>
                    </div>
                </div>
            </nav>

            {/* Main Content */}
            <main className="container mx-auto px-4 py-8">
                {/* Header */}
                <div className="text-center mb-12">
                    <h1 className="text-5xl md:text-6xl font-bold mb-4 hero-text">Universe News</h1>
                    <p className="text-xl text-gray-300 max-w-3xl mx-auto">
                        Stay updated with the latest space discoveries, missions, and astronomical breakthroughs
                    </p>
                </div>

                {/* Filters */}
                <div className="glass-panel p-6 rounded-2xl mb-8">
                    <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                        <div>
                            <label className="block text-sm font-medium text-gray-300 mb-2">Filter by Source</label>
                            <select
                                value={selectedSource}
                                onChange={handleSourceChange}
                                className="w-full bg-gray-800 border border-gray-700 rounded-lg px-4 py-2 text-white focus:outline-none focus:ring-2 focus:ring-blue-500"
                            >
                                <option value="">All Sources</option>
                                {sources.map(source => (
                                    <option key={source} value={source}>{source}</option>
                                ))}
                            </select>
                        </div>
                        <div>
                            <label className="block text-sm font-medium text-gray-300 mb-2">Filter by Category</label>
                            <select
                                value={selectedCategory}
                                onChange={handleCategoryChange}
                                className="w-full bg-gray-800 border border-gray-700 rounded-lg px-4 py-2 text-white focus:outline-none focus:ring-2 focus:ring-blue-500"
                            >
                                <option value="">All Categories</option>
                                {categories.map(category => (
                                    <option key={category} value={category}>{category}</option>
                                ))}
                            </select>
                        </div>
                    </div>
                </div>

                {/* Loading State */}
                {loading && (
                    <div className="text-center py-20">
                        <div className="inline-block animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-blue-500"></div>
                        <p className="mt-4 text-gray-400">Loading news...</p>
                    </div>
                )}

                {/* Error State */}
                {error && (
                    <div className="glass-panel p-8 rounded-2xl text-center">
                        <p className="text-red-400 text-lg">Error: {error}</p>
                        <button
                            onClick={fetchNews}
                            className="mt-4 btn-primary px-6 py-2 rounded-lg text-white font-semibold"
                        >
                            Try Again
                        </button>
                    </div>
                )}

                {/* News Grid */}
                {!loading && !error && news.length > 0 && (
                    <div className="space-y-6">
                        {news.map(article => (
                            <div
                                key={article.id}
                                className="glass-panel news-card p-6 rounded-2xl border border-gray-700/50 hover:border-blue-500/50 transition-all cursor-pointer"
                                onClick={() => navigate(`/news/${article.id}`)}
                            >
                                <div className="flex flex-col md:flex-row gap-6">
                                    {article.imageUrl && (
                                        <div className="md:w-1/3">
                                            <img
                                                src={article.imageUrl}
                                                alt={article.title}
                                                className="w-full h-48 object-cover rounded-lg"
                                                onError={(e) => { e.target.style.display = 'none'; }}
                                            />
                                        </div>
                                    )}
                                    <div className={article.imageUrl ? 'md:w-2/3' : 'w-full'}>
                                        <div className="flex items-center gap-3 mb-3">
                                            <span className={`badge ${getSourceBadgeClass(article.source)}`}>
                                                {article.source}
                                            </span>
                                            {article.category && (
                                                <span className="badge badge-category">{article.category}</span>
                                            )}
                                            <span className="text-sm text-gray-500">{formatDate(article.publishedDate)}</span>
                                        </div>
                                        <h2 className="text-2xl font-bold text-white mb-3 hover:text-blue-400 transition-colors">
                                            {article.title}
                                        </h2>
                                        <p className="text-gray-300 mb-4 line-clamp-3">{article.summary}</p>
                                        <button className="text-blue-400 hover:text-blue-300 font-semibold flex items-center gap-2">
                                            Read More
                                            <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 5l7 7-7 7" />
                                            </svg>
                                        </button>
                                    </div>
                                </div>
                            </div>
                        ))}
                    </div>
                )}

                {/* Empty State */}
                {!loading && !error && news.length === 0 && (
                    <div className="glass-panel p-12 rounded-2xl text-center">
                        <p className="text-gray-400 text-lg">No news articles found</p>
                        <p className="text-gray-500 mt-2">Try adjusting your filters</p>
                    </div>
                )}

                {/* Pagination */}
                {!loading && !error && totalPages > 1 && (
                    <div className="flex justify-center items-center gap-4 mt-12">
                        <button
                            onClick={() => handlePageChange(currentPage - 1)}
                            disabled={currentPage === 0}
                            className="px-4 py-2 rounded-lg bg-gray-800 text-white disabled:opacity-50 disabled:cursor-not-allowed hover:bg-gray-700 transition-colors"
                        >
                            Previous
                        </button>
                        <span className="text-gray-300">
                            Page {currentPage + 1} of {totalPages}
                        </span>
                        <button
                            onClick={() => handlePageChange(currentPage + 1)}
                            disabled={currentPage >= totalPages - 1}
                            className="px-4 py-2 rounded-lg bg-gray-800 text-white disabled:opacity-50 disabled:cursor-not-allowed hover:bg-gray-700 transition-colors"
                        >
                            Next
                        </button>
                    </div>
                )}
            </main>

            {/* Footer */}
            <footer className="border-t border-gray-800 mt-20 bg-black/40 backdrop-blur-sm">
                <div className="max-w-7xl mx-auto px-4 py-8 sm:px-6 lg:px-8 flex flex-col md:flex-row justify-between items-center">
                    <div className="mb-4 md:mb-0">
                        <span className="text-xl font-bold text-white">Universe Odyssey</span>
                        <p className="text-sm text-gray-500 mt-1">© 2025 All rights reserved.</p>
                    </div>
                    <div className="flex space-x-6">
                        <a href="#" className="text-gray-400 hover:text-white transition-colors">Privacy</a>
                        <a href="#" className="text-gray-400 hover:text-white transition-colors">Terms</a>
                        <a href="#" className="text-gray-400 hover:text-white transition-colors">Contact</a>
                    </div>
                </div>
            </footer>
        </div>
    );
};

export default News;
