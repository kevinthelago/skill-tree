'use client';

import { useState } from 'react';
import Header from './components/Header';
import Sidebar from './components/Sidebar';
import ProgressPanel from './components/ProgressPanel';

export default function Home() {
  const [selectedItem, setSelectedItem] = useState<any>(null);
  const [sidebarCollapsed, setSidebarCollapsed] = useState(false);

  return (
    <div className="min-h-screen bg-slate-950 flex flex-col">
      {/* Header */}
      <Header />

      {/* Main Content Area */}
      <div className="flex flex-1 overflow-hidden">
        {/* Left Sidebar */}
        <Sidebar
          collapsed={sidebarCollapsed}
          onToggleCollapse={() => setSidebarCollapsed(!sidebarCollapsed)}
          onSelectItem={setSelectedItem}
          selectedItem={selectedItem}
        />

        {/* Center Content - Graph Visualization */}
        <main className="flex-1 p-6 overflow-auto">
          <div className="h-full flex items-center justify-center">
            <div className="text-center">
              <h2 className="text-2xl font-bold text-slate-200 mb-4">
                Graph Visualization
              </h2>
              <p className="text-slate-400">
                Coming soon...
              </p>
            </div>
          </div>
        </main>

        {/* Right Progress Panel */}
        <ProgressPanel selectedItem={selectedItem} />
      </div>
    </div>
  );
}
