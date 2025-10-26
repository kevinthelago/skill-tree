'use client';

import { useEffect, useState } from 'react';

interface UserProfile {
  username: string;
  email: string;
  role: string;
  thumbnailUrl?: string;
}

export default function Header() {
  const [user, setUser] = useState<UserProfile | null>(null);

  useEffect(() => {
    // Fetch user profile
    fetch('/api/progress/profile', {
      credentials: 'include'
    })
      .then(res => res.json())
      .then(data => setUser(data))
      .catch(err => console.error('Failed to fetch user profile:', err));
  }, []);

  const handleLogout = () => {
    window.location.href = '/api/auth/logout';
  };

  return (
    <header className="h-16 bg-slate-900 border-b border-blue-500/30 flex items-center justify-between px-6">
      {/* Left - Logo/Title */}
      <div className="flex items-center gap-4">
        <div className="text-xl font-bold text-blue-400">
          SKILL<span className="text-purple-400">TREE</span>
        </div>
      </div>

      {/* Right - User Info */}
      <div className="flex items-center gap-4">
        {user && (
          <>
            <div className="text-right">
              <div className="text-sm font-medium text-slate-200">{user.username}</div>
              <div className="text-xs text-slate-400">{user.role}</div>
            </div>

            {/* User Thumbnail */}
            <div className="relative group">
              {user.thumbnailUrl ? (
                <img
                  src={user.thumbnailUrl}
                  alt={user.username}
                  className="w-10 h-10 rounded-full border-2 border-blue-500/50 group-hover:border-blue-500 transition-all"
                />
              ) : (
                <div className="w-10 h-10 rounded-full border-2 border-blue-500/50 group-hover:border-blue-500 transition-all bg-slate-800 flex items-center justify-center">
                  <span className="text-blue-400 font-bold text-lg">
                    {user.username.charAt(0).toUpperCase()}
                  </span>
                </div>
              )}
            </div>

            {/* Logout Button */}
            <button
              onClick={handleLogout}
              className="px-4 py-2 bg-slate-800 hover:bg-slate-700 border border-slate-700 hover:border-red-500/50 rounded text-sm text-slate-300 hover:text-red-400 transition-all"
            >
              Logout
            </button>
          </>
        )}
      </div>
    </header>
  );
}
