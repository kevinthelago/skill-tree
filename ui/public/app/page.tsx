import Link from 'next/link';
import SciFiScreen from './components/SciFiScreen';

export default function Home() {
  return (
    <div className="min-h-screen bg-slate-950 flex flex-col items-center justify-center p-8 gap-8">
      {/* Hero Section */}
      <div className="flex flex-wrap justify-center items-center gap-8 max-w-7xl">
        <SciFiScreen title="Skill Tree" screenStack={5}>
          <div className="flex flex-col gap-4">
            <p className="text-sm text-slate-300 leading-relaxed">
              Master your learning journey through structured skill development and curated resources.
            </p>
            <div className="flex flex-col gap-2 mt-4">
              <Link
                href="/login"
                className="px-6 py-3 bg-blue-500/20 hover:bg-blue-500/30 border border-blue-500/30 rounded-lg text-center text-sm font-medium transition-all duration-200"
              >
                Sign In
              </Link>
              <Link
                href="/signup"
                className="px-6 py-3 bg-purple-500/20 hover:bg-purple-500/30 border border-purple-500/30 rounded-lg text-center text-sm font-medium transition-all duration-200"
              >
                Get Started
              </Link>
            </div>
          </div>
        </SciFiScreen>

        <SciFiScreen title="Features" screenStack={4}>
          <div className="flex flex-col gap-3 text-xs">
            <div className="flex items-start gap-2">
              <div className="w-2 h-2 mt-1 rounded-full bg-cyan-400"></div>
              <div>
                <h3 className="font-semibold text-cyan-400 mb-1">Hierarchical Learning</h3>
                <p className="text-slate-400">Organize knowledge from domains to microskills</p>
              </div>
            </div>
            <div className="flex items-start gap-2">
              <div className="w-2 h-2 mt-1 rounded-full bg-purple-400"></div>
              <div>
                <h3 className="font-semibold text-purple-400 mb-1">Curated Resources</h3>
                <p className="text-slate-400">Access quality learning materials and references</p>
              </div>
            </div>
            <div className="flex items-start gap-2">
              <div className="w-2 h-2 mt-1 rounded-full bg-blue-400"></div>
              <div>
                <h3 className="font-semibold text-blue-400 mb-1">Custom Curricula</h3>
                <p className="text-slate-400">Build personalized learning paths</p>
              </div>
            </div>
          </div>
        </SciFiScreen>
      </div>

      {/* Additional Info Section */}
      <div className="flex flex-wrap justify-center items-start gap-8 max-w-7xl">
        <SciFiScreen title="Explore" screenStack={3}>
          <div className="flex flex-col gap-2 text-xs">
            <p className="text-slate-300">
              Browse through organized learning domains and discover structured paths to mastery.
            </p>
            <button className="mt-4 px-4 py-2 bg-cyan-500/20 hover:bg-cyan-500/30 border border-cyan-500/30 rounded-lg text-sm font-medium transition-all duration-200">
              View Domains
            </button>
          </div>
        </SciFiScreen>

        <SciFiScreen title="Learn" screenStack={3}>
          <div className="flex flex-col gap-2 text-xs">
            <p className="text-slate-300">
              Access curated curricula designed by experts to guide your learning journey.
            </p>
            <button className="mt-4 px-4 py-2 bg-purple-500/20 hover:bg-purple-500/30 border border-purple-500/30 rounded-lg text-sm font-medium transition-all duration-200">
              Browse Curricula
            </button>
          </div>
        </SciFiScreen>

        <SciFiScreen title="Track" screenStack={3}>
          <div className="flex flex-col gap-2 text-xs">
            <p className="text-slate-300">
              Monitor your progress and achievements as you advance through skills.
            </p>
            <button className="mt-4 px-4 py-2 bg-blue-500/20 hover:bg-blue-500/30 border border-blue-500/30 rounded-lg text-sm font-medium transition-all duration-200">
              Get Started
            </button>
          </div>
        </SciFiScreen>
      </div>
    </div>
  );
}
