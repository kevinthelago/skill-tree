'use client';

import { useState } from 'react';
import Link from 'next/link';
import SciFiScreen from '../components/SciFiScreen';

export default function SignupPage() {
  const [username, setUsername] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    if (password !== confirmPassword) {
      alert('Passwords do not match');
      return;
    }

    // TODO: Implement signup logic with Spring Boot backend
    console.log('Signup:', { username, email, password });
  };

  return (
    <div className="min-h-screen bg-slate-950 flex items-center justify-center p-8">
      <SciFiScreen title="Sign Up" screenStack={5}>
        <form onSubmit={handleSubmit} className="flex flex-col gap-4">
          {/* Username Input */}
          <div className="flex flex-col gap-1">
            <label htmlFor="username" className="text-xs text-slate-400 font-medium">
              Username
            </label>
            <div className="flex items-center gap-2 border-b border-purple-500/30 pb-2 transition-all duration-200 hover:border-purple-500/50">
              <svg
                className="w-4 h-4 text-slate-400"
                xmlns="http://www.w3.org/2000/svg"
                viewBox="0 0 512 512"
                fill="currentColor"
              >
                <path d="M256 0a128 128 0 1 1 0 256 128 128 0 1 1 0-256zm-96 320a96 96 0 1 1 0 192 96 96 0 1 1 0-192zm224 0a96 96 0 1 1 0 192 96 96 0 1 1 0-192z" />
              </svg>
              <input
                id="username"
                type="text"
                value={username}
                onChange={(e) => setUsername(e.target.value)}
                placeholder="johndoe"
                required
                minLength={3}
                className="flex-1 bg-transparent border-none outline-none text-sm text-slate-200 placeholder-slate-500"
              />
            </div>
          </div>

          {/* Email Input */}
          <div className="flex flex-col gap-1">
            <label htmlFor="email" className="text-xs text-slate-400 font-medium">
              Email Address
            </label>
            <div className="flex items-center gap-2 border-b border-purple-500/30 pb-2 transition-all duration-200 hover:border-purple-500/50">
              <svg
                className="w-4 h-4 text-slate-400"
                xmlns="http://www.w3.org/2000/svg"
                viewBox="0 0 512 512"
                fill="currentColor"
              >
                <path d="M48 64C21.5 64 0 85.5 0 112c0 15.1 7.1 29.3 19.2 38.4L236.8 313.6c11.4 8.5 27 8.5 38.4 0L492.8 150.4c12.1-9.1 19.2-23.3 19.2-38.4c0-26.5-21.5-48-48-48H48zM0 176V384c0 35.3 28.7 64 64 64H448c35.3 0 64-28.7 64-64V176L294.4 339.2c-22.8 17.1-54 17.1-76.8 0L0 176z" />
              </svg>
              <input
                id="email"
                type="email"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                placeholder="your@email.com"
                required
                className="flex-1 bg-transparent border-none outline-none text-sm text-slate-200 placeholder-slate-500"
              />
            </div>
          </div>

          {/* Password Input */}
          <div className="flex flex-col gap-1">
            <label htmlFor="password" className="text-xs text-slate-400 font-medium">
              Password
            </label>
            <div className="flex items-center gap-2 border-b border-purple-500/30 pb-2 transition-all duration-200 hover:border-purple-500/50">
              <svg
                className="w-4 h-4 text-slate-400"
                xmlns="http://www.w3.org/2000/svg"
                viewBox="0 0 512 512"
                fill="currentColor"
              >
                <path d="M144 144v48H304V144c0-44.2-35.8-80-80-80s-80 35.8-80 80zM80 192V144C80 64.5 144.5 0 224 0s144 64.5 144 144v48h16c35.3 0 64 28.7 64 64V448c0 35.3-28.7 64-64 64H64c-35.3 0-64-28.7-64-64V256c0-35.3 28.7-64 64-64H80z" />
              </svg>
              <input
                id="password"
                type="password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                placeholder="••••••••"
                required
                minLength={8}
                className="flex-1 bg-transparent border-none outline-none text-sm text-slate-200 placeholder-slate-500"
              />
            </div>
            <p className="text-xs text-slate-500 mt-1">At least 8 characters</p>
          </div>

          {/* Confirm Password Input */}
          <div className="flex flex-col gap-1">
            <label htmlFor="confirmPassword" className="text-xs text-slate-400 font-medium">
              Confirm Password
            </label>
            <div className="flex items-center gap-2 border-b border-purple-500/30 pb-2 transition-all duration-200 hover:border-purple-500/50">
              <svg
                className="w-4 h-4 text-slate-400"
                xmlns="http://www.w3.org/2000/svg"
                viewBox="0 0 512 512"
                fill="currentColor"
              >
                <path d="M336 352c97.2 0 176-78.8 176-176S433.2 0 336 0S160 78.8 160 176c0 18.7 2.9 36.8 8.3 53.7L7 391c-4.5 4.5-7 10.6-7 17v80c0 13.3 10.7 24 24 24h80c13.3 0 24-10.7 24-24V448h40c13.3 0 24-10.7 24-24V384h40c6.4 0 12.5-2.5 17-7l33.3-33.3c16.9 5.4 35 8.3 53.7 8.3zM376 96a40 40 0 1 1 0 80 40 40 0 1 1 0-80z" />
              </svg>
              <input
                id="confirmPassword"
                type="password"
                value={confirmPassword}
                onChange={(e) => setConfirmPassword(e.target.value)}
                placeholder="••••••••"
                required
                minLength={8}
                className="flex-1 bg-transparent border-none outline-none text-sm text-slate-200 placeholder-slate-500"
              />
            </div>
          </div>

          {/* Terms & Conditions */}
          <label className="flex items-start gap-2 text-xs text-slate-400 cursor-pointer mt-2">
            <input
              type="checkbox"
              required
              className="w-3 h-3 mt-0.5 rounded border-purple-500/30 bg-transparent"
            />
            <span>
              I agree to the{' '}
              <Link href="/terms" className="text-purple-400 hover:text-purple-300 transition-colors">
                Terms of Service
              </Link>
              {' '}and{' '}
              <Link href="/privacy" className="text-purple-400 hover:text-purple-300 transition-colors">
                Privacy Policy
              </Link>
            </span>
          </label>

          {/* Sign Up Button */}
          <button
            type="submit"
            className="mt-4 px-6 py-3 bg-purple-500/20 hover:bg-purple-500/30 border border-purple-500/30 rounded-lg text-sm font-medium transition-all duration-200 hover:scale-105"
          >
            Create Account
          </button>

          {/* Divider */}
          <div className="flex items-center gap-2 my-2">
            <div className="flex-1 h-px bg-slate-700"></div>
            <span className="text-xs text-slate-500">or sign up with</span>
            <div className="flex-1 h-px bg-slate-700"></div>
          </div>

          {/* OAuth Options */}
          <div className="flex flex-col gap-2">
            <button
              type="button"
              className="flex items-center justify-center gap-2 px-4 py-2 bg-slate-800/50 hover:bg-slate-800 border border-slate-700 rounded-lg text-sm transition-all duration-200"
            >
              <svg className="w-4 h-4" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 512 512" fill="currentColor">
                <path d="M201.5 305.5c-13.8 0-24.9-11.1-24.9-24.6 0-13.8 11.1-24.9 24.9-24.9 13.6 0 24.6 11.1 24.6 24.9 0 13.6-11.1 24.6-24.6 24.6zM504 256c0 137-111 248-248 248S8 393 8 256 119 8 256 8s248 111 248 248zm-132.3-41.2c-9.4 0-17.7 3.9-23.8 10-22.4-15.5-52.6-25.5-86.1-26.6l17.4-78.3 55.4 12.5c0 13.6 11.1 24.6 24.6 24.6 13.8 0 24.9-11.3 24.9-24.9s-11.1-24.9-24.9-24.9c-9.7 0-18 5.8-22.1 13.8l-61.2-13.6c-3-.8-6.1 1.4-6.9 4.4l-19.1 86.4c-33.2 1.4-63.1 11.3-85.5 26.8-6.1-6.4-14.7-10.2-24.1-10.2-34.9 0-46.3 46.9-14.4 62.8-1.1 5-1.7 10.2-1.7 15.5 0 52.6 59.2 95.2 132 95.2 73.1 0 132.3-42.6 132.3-95.2 0-5.3-.6-10.8-1.9-15.8 31.3-16 19.8-62.5-14.9-62.5zM302.8 331c-18.2 18.2-76.1 17.9-93.6 0-2.2-2.2-6.1-2.2-8.3 0-2.5 2.5-2.5 6.4 0 8.6 22.8 22.8 87.3 22.8 110.2 0 2.5-2.2 2.5-6.1 0-8.6-2.2-2.2-6.1-2.2-8.3 0zm7.7-75c-13.6 0-24.6 11.1-24.6 24.9 0 13.6 11.1 24.6 24.6 24.6 13.8 0 24.9-11.1 24.9-24.6 0-13.8-11-24.9-24.9-24.9z" />
              </svg>
              Sign up with GitHub
            </button>
            <button
              type="button"
              className="flex items-center justify-center gap-2 px-4 py-2 bg-slate-800/50 hover:bg-slate-800 border border-slate-700 rounded-lg text-sm transition-all duration-200"
            >
              <svg className="w-4 h-4" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 512 512" fill="currentColor">
                <path d="M488 261.8C488 403.3 391.1 504 248 504 110.8 504 0 393.2 0 256S110.8 8 248 8c66.8 0 123 24.5 166.3 64.9l-67.5 64.9C258.5 52.6 94.3 116.6 94.3 256c0 86.5 69.1 156.6 153.7 156.6 98.2 0 135-70.4 140.8-106.9H248v-85.3h236.1c2.3 12.7 3.9 24.9 3.9 41.4z" />
              </svg>
              Sign up with Google
            </button>
          </div>

          {/* Sign In Link */}
          <div className="text-center mt-4 text-xs text-slate-400">
            Already have an account?{' '}
            <Link href="/login" className="text-blue-400 hover:text-blue-300 transition-colors font-medium">
              Sign in
            </Link>
          </div>

          {/* Back to Home */}
          <Link
            href="/"
            className="text-center mt-2 text-xs text-slate-500 hover:text-slate-400 transition-colors"
          >
            ← Back to home
          </Link>
        </form>
      </SciFiScreen>
    </div>
  );
}
