'use client';

interface ProgressPanelProps {
  selectedItem: any;
}

export default function ProgressPanel({ selectedItem }: ProgressPanelProps) {
  if (!selectedItem) {
    return (
      <aside className="w-80 bg-slate-900 border-l border-purple-500/30 p-6 overflow-y-auto">
        <div className="h-full flex items-center justify-center">
          <div className="text-center text-slate-400">
            <p className="text-sm">Select an item</p>
            <p className="text-xs mt-2">to view progress details</p>
          </div>
        </div>
      </aside>
    );
  }

  const getTypeLabel = (type: string) => {
    switch (type) {
      case 'domain': return 'Domain';
      case 'category': return 'Category';
      case 'subcategory': return 'Subcategory';
      case 'skill': return 'Skill';
      case 'microskill': return 'Microskill';
      default: return 'Item';
    }
  };

  const getTypeColor = (type: string) => {
    switch (type) {
      case 'domain': return 'text-cyan-400';
      case 'category': return 'text-purple-400';
      case 'subcategory': return 'text-blue-400';
      case 'skill': return 'text-green-400';
      case 'microskill': return 'text-yellow-400';
      default: return 'text-slate-400';
    }
  };

  const getProgressColor = (percentage: number) => {
    if (percentage === 100) return 'from-green-500 to-emerald-500';
    if (percentage >= 50) return 'from-yellow-500 to-orange-500';
    return 'from-blue-500 to-purple-500';
  };

  const renderChildren = () => {
    if (selectedItem.type === 'domain' && selectedItem.categories) {
      return (
        <div className="space-y-2">
          <h3 className="text-sm font-semibold text-slate-300 mb-2">Categories ({selectedItem.categories.length})</h3>
          {selectedItem.categories.map((cat: any) => (
            <div key={cat.id} className="flex items-center justify-between p-2 bg-slate-800/50 rounded border border-slate-700">
              <span className="text-sm text-slate-200">{cat.name}</span>
              <span className="text-xs text-purple-400">{Math.round(cat.progressPercentage)}%</span>
            </div>
          ))}
        </div>
      );
    }

    if (selectedItem.type === 'category' && selectedItem.subcategories) {
      return (
        <div className="space-y-2">
          <h3 className="text-sm font-semibold text-slate-300 mb-2">Subcategories ({selectedItem.subcategories.length})</h3>
          {selectedItem.subcategories.map((sub: any) => (
            <div key={sub.id} className="flex items-center justify-between p-2 bg-slate-800/50 rounded border border-slate-700">
              <span className="text-sm text-slate-200">{sub.name}</span>
              <span className="text-xs text-blue-400">{Math.round(sub.progressPercentage)}%</span>
            </div>
          ))}
        </div>
      );
    }

    if (selectedItem.type === 'subcategory' && selectedItem.skills) {
      return (
        <div className="space-y-2">
          <h3 className="text-sm font-semibold text-slate-300 mb-2">Skills ({selectedItem.skills.length})</h3>
          {selectedItem.skills.map((skill: any) => (
            <div key={skill.id} className="flex items-center justify-between p-2 bg-slate-800/50 rounded border border-slate-700">
              <span className="text-sm text-slate-200">{skill.name}</span>
              <span className="text-xs text-green-400">{Math.round(skill.progressPercentage)}%</span>
            </div>
          ))}
        </div>
      );
    }

    if (selectedItem.type === 'skill' && selectedItem.microskills) {
      return (
        <div className="space-y-2">
          <h3 className="text-sm font-semibold text-slate-300 mb-2">Microskills ({selectedItem.microskills.length})</h3>
          {selectedItem.microskills.map((micro: any) => (
            <div key={micro.id} className="flex items-center justify-between p-2 bg-slate-800/50 rounded border border-slate-700">
              <span className="text-sm text-slate-200">{micro.name}</span>
              <div className="flex items-center gap-2">
                {micro.completed && <span className="text-green-400 text-xs">✓</span>}
                <span className="text-xs text-yellow-400">{Math.round(micro.progressPercentage)}%</span>
              </div>
            </div>
          ))}
        </div>
      );
    }

    return null;
  };

  return (
    <aside className="w-96 bg-slate-900 border-l border-purple-500/30 overflow-y-auto">
      <div className="p-6">
        {/* Header */}
        <div className="mb-6">
          <div className={`text-xs font-semibold uppercase tracking-wider mb-2 ${getTypeColor(selectedItem.type)}`}>
            {getTypeLabel(selectedItem.type)}
          </div>
          <h2 className="text-2xl font-bold text-slate-100 mb-3">{selectedItem.name}</h2>

          {selectedItem.description && (
            <p className="text-sm text-slate-400 leading-relaxed">{selectedItem.description}</p>
          )}
        </div>

        {/* Progress Section */}
        <div className="mb-6 p-4 bg-slate-800/50 rounded-lg border border-slate-700">
          <div className="flex items-center justify-between mb-3">
            <span className="text-sm text-slate-300 font-medium">Progress</span>
            <div className="flex items-center gap-2">
              {selectedItem.completed && (
                <span className="text-green-400 text-sm">✓ Completed</span>
              )}
              <span className="text-lg font-bold text-slate-100">
                {Math.round(selectedItem.progressPercentage)}%
              </span>
            </div>
          </div>

          {/* Progress Bar */}
          <div className="w-full h-3 bg-slate-700 rounded-full overflow-hidden">
            <div
              className={`h-full bg-gradient-to-r ${getProgressColor(selectedItem.progressPercentage)} transition-all duration-300`}
              style={{ width: `${selectedItem.progressPercentage}%` }}
            />
          </div>
        </div>

        {/* Last Accessed */}
        {selectedItem.lastAccessed && (
          <div className="mb-6 text-xs text-slate-400">
            <span className="font-medium">Last accessed:</span>{' '}
            {new Date(selectedItem.lastAccessed).toLocaleDateString('en-US', {
              year: 'numeric',
              month: 'short',
              day: 'numeric',
              hour: '2-digit',
              minute: '2-digit'
            })}
          </div>
        )}

        {/* Children Items */}
        {renderChildren()}

        {/* Actions */}
        <div className="mt-6 pt-6 border-t border-slate-700">
          <button className="w-full px-4 py-2 bg-blue-500/20 hover:bg-blue-500/30 border border-blue-500/30 rounded-lg text-sm font-medium text-blue-400 hover:text-blue-300 transition-all">
            Mark as Complete
          </button>
        </div>
      </div>
    </aside>
  );
}
