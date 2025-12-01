import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import './NewsDetail.css';

const NewsDetail = () => {
    const { id } = useParams();
    const navigate = useNavigate();
    const [article, setArticle] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        fetchArticle();
    }, [id]);

    const fetchArticle = async () => {
        setLoading(true);
        setError(null);

        try {
            const response = await fetch(`/api/news/${id}`);
            if (!response.ok) throw new Error('Article not found');

            const data = await response.json();
            setArticle(data);
            setLoading(false);
        } catch (err) {
            setError(err.message);
            setLoading(false);
        }
    };

    const formatDate = (dateString) => {
        const date = new Date(dateString);
        return date.toLocaleDateString('en-US', {
            year: 'numeric',
            month: 'long',
            day: 'numeric',
            hour: '2-digit',
            minute: '2-digit'
        });
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

    const shareOnTwitter = () => {
        const text = encodeURIComponent(article.title);
        const url = encodeURIComponent(window.location.href);
        window.open(`https://twitter.com/intent/tweet?text=${text}&url=${url}`, '_blank');
    };

    const shareOnFacebook = () => {
        const url = encodeURIComponent(window.location.href);
        window.open(`https://www.facebook.com/sharer/sharer.php?u=${url}`, '_blank');
    };

    const shareOnLinkedIn = () => {
        const url = encodeURIComponent(window.location.href);
        window.open(`https://www.linkedin.com/sharing/share-offsite/?url=${url}`, '_blank');
    };

    if (loading) {
        return (
            <div className="news-detail-page">
                <div className="container mx-auto px-4 py-20 text-center">
                    <div className="inline-block animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-blue-500"></div>
                    <p className="mt-4 text-gray-400">Loading article...</p>
                </div>
            </div>
        );
    }

    if (error || !article) {
        return (
            <div className="news-detail-page">
                <div className="container mx-auto px-4 py-20">
                    <div className="glass-panel p-12 rounded-2xl text-center">
                        <p className="text-red-400 text-xl mb-4">Article not found</p>
                        <button
                            onClick={() => navigate('/news')}
                            className="btn-primary px-6 py-3 rounded-lg text-white font-semibold"
                        >
                            Back to News
                        </button>
                    </div>
                </div>
            </div>
        );
    }

    return (
        <div className="news-detail-page">
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
            <main className="container mx-auto px-4 py-8 max-w-4xl">
                {/* Back Button */}
                <button
                    onClick={() => navigate('/news')}
                    className="flex items-center gap-2 text-gray-400 hover:text-white mb-8 transition-colors"
                >
                    <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M15 19l-7-7 7-7" />
                    </svg>
                    Back to News
                </button>

                {/* Article */}
                <article className="glass-panel p-8 rounded-2xl">
                    {/* Header */}
                    <div className="mb-6">
                        <div className="flex items-center gap-3 mb-4">
                            <span className={`badge ${getSourceBadgeClass(article.source)}`}>
                                {article.source}
                            </span>
                            {article.category && (
                                <span className="badge badge-category">{article.category}</span>
                            )}
                        </div>
                        <h1 className="text-4xl md:text-5xl font-bold text-white mb-4 leading-tight">
                            {article.title}
                        </h1>
                        <p className="text-gray-400 text-sm">
                            Published on {formatDate(article.publishedDate)}
                        </p>
                    </div>

                    {/* Featured Image */}
                    {article.imageUrl && (
                        <div className="mb-8">
                            <img
                                src={article.imageUrl}
                                alt={article.title}
                                className="w-full rounded-lg shadow-2xl"
                                onError={(e) => { e.target.style.display = 'none'; }}
                            />
                        </div>
                    )}

                    {/* Content */}
                    <div className="prose prose-invert max-w-none mb-8">
                        <p className="text-lg text-gray-300 leading-relaxed whitespace-pre-wrap">
                            {article.content}
                        </p>
                    </div>

                    {/* Metadata */}
                    <div className="border-t border-gray-700 pt-6 mb-6">
                        <div className="grid grid-cols-1 md:grid-cols-3 gap-4 text-sm">
                            <div>
                                <span className="text-gray-500">Source:</span>
                                <span className="text-white ml-2">{article.source}</span>
                            </div>
                            <div>
                                <span className="text-gray-500">Category:</span>
                                <span className="text-white ml-2">{article.category || 'General'}</span>
                            </div>
                            <div>
                                <span className="text-gray-500">Published:</span>
                                <span className="text-white ml-2">{formatDate(article.publishedDate)}</span>
                            </div>
                        </div>
                    </div>

                    {/* Actions */}
                    <div className="flex flex-col sm:flex-row gap-4 items-center justify-between border-t border-gray-700 pt-6">
                        <a
                            href={article.originalUrl}
                            target="_blank"
                            rel="noopener noreferrer"
                            className="btn-primary px-6 py-3 rounded-lg text-white font-semibold flex items-center gap-2 hover:shadow-lg transition-all"
                        >
                            Read Original Article
                            <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M10 6H6a2 2 0 00-2 2v10a2 2 0 002 2h10a2 2 0 002-2v-4M14 4h6m0 0v6m0-6L10 14" />
                            </svg>
                        </a>

                        {/* Social Share Buttons */}
                        <div className="flex gap-3">
                            <button
                                onClick={shareOnTwitter}
                                className="share-btn share-twitter"
                                title="Share on Twitter"
                            >
                                <svg className="w-5 h-5" fill="currentColor" viewBox="0 0 24 24">
                                    <path d="M23 3a10.9 10.9 0 01-3.14 1.53 4.48 4.48 0 00-7.86 3v1A10.66 10.66 0 013 4s-4 9 5 13a11.64 11.64 0 01-7 2c9 5 20 0 20-11.5a4.5 4.5 0 00-.08-.83A7.72 7.72 0 0023 3z" />
                                </svg>
                            </button>
                            <button
                                onClick={shareOnFacebook}
                                className="share-btn share-facebook"
                                title="Share on Facebook"
                            >
                                <svg className="w-5 h-5" fill="currentColor" viewBox="0 0 24 24">
                                    <path d="M18 2h-3a5 5 0 00-5 5v3H7v4h3v8h4v-8h3l1-4h-4V7a1 1 0 011-1h3z" />
                                </svg>
                            </button>
                            <button
                                onClick={shareOnLinkedIn}
                                className="share-btn share-linkedin"
                                title="Share on LinkedIn"
                            >
                                <svg className="w-5 h-5" fill="currentColor" viewBox="0 0 24 24">
                                    <path d="M16 8a6 6 0 016 6v7h-4v-7a2 2 0 00-2-2 2 2 0 00-2 2v7h-4v-7a6 6 0 016-6zM2 9h4v12H2z" />
                                    <circle cx="4" cy="4" r="2" />
                                </svg>
                            </button>
                        </div>
                    </div>
                </article>
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

export default NewsDetail;
