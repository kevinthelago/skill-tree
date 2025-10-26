'use client';

import { useEffect, useState } from 'react';

interface SidebarProps {
  collapsed: boolean;
  onToggleCollapse: () => void;
  onSelectItem: (item: any) => void;
  selectedItem: any;
}

interface MicroskillProgress {
  id: number;
  name: string;
  completed: boolean;
  progressPercentage: number;
}

interface SkillProgress {
  id: number;
  name: string;
  completed: boolean;
  progressPercentage: number;
  microskills: MicroskillProgress[];
}

interface SubcategoryProgress {
  id: number;
  name: string;
  completed: boolean;
  progressPercentage: number;
  skills: SkillProgress[];
}

interface CategoryProgress {
  id: number;
  name: string;
  completed: boolean;
  progressPercentage: number;
  subcategories: SubcategoryProgress[];
}

interface DomainProgress {
  id: number;
  name: string;
  completed: boolean;
  progressPercentage: number;
  categories: CategoryProgress[];
}

interface ProgressTree {
  domains: DomainProgress[];
  overallProgress: number;
}

export default function Sidebar({ collapsed, onToggleCollapse, onSelectItem, selectedItem }: SidebarProps) {
  const [progressTree, setProgressTree] = useState<ProgressTree | null>(null);
  const [expandedDomains, setExpandedDomains] = useState<Set<number>>(new Set());
  const [expandedCategories, setExpandedCategories] = useState<Set<number>>(new Set());
  const [expandedSubcategories, setExpandedSubcategories] = useState<Set<number>>(new Set());
  const [expandedSkills, setExpandedSkills] = useState<Set<number>>(new Set());

  useEffect(() => {
    // Fetch progress tree
    fetch('/api/progress/me', {
      credentials: 'include'
    })
      .then(res => res.json())
      .then(data => setProgressTree(data))
      .catch(err => console.error('Failed to fetch progress tree:', err));
  }, []);

  const toggleDomain = (domainId: number) => {
    const newExpanded = new Set(expandedDomains);
    if (newExpanded.has(domainId)) {
      newExpanded.delete(domainId);
    } else {
      newExpanded.add(domainId);
    }
    setExpandedDomains(newExpanded);
  };

  const toggleCategory = (categoryId: number) => {
    const newExpanded = new Set(expandedCategories);
    if (newExpanded.has(categoryId)) {
      newExpanded.delete(categoryId);
    } else {
      newExpanded.add(categoryId);
    }
    setExpandedCategories(newExpanded);
  };

  const toggleSubcategory = (subcategoryId: number) => {
    const newExpanded = new Set(expandedSubcategories);
    if (newExpanded.has(subcategoryId)) {
      newExpanded.delete(subcategoryId);
    } else {
      newExpanded.add(subcategoryId);
    }
    setExpandedSubcategories(newExpanded);
  };

  const toggleSkill = (skillId: number) => {
    const newExpanded = new Set(expandedSkills);
    if (newExpanded.has(skillId)) {
      newExpanded.delete(skillId);
    } else {
      newExpanded.add(skillId);
    }
    setExpandedSkills(newExpanded);
  };

  const getProgressColor = (percentage: number) => {
    if (percentage === 100) return 'text-green-400';
    if (percentage >= 50) return 'text-yellow-400';
    return 'text-blue-400';
  };

  const renderMicroskill = (microskill: MicroskillProgress, indent: number) => {
    const isSelected = selectedItem?.id === microskill.id && selectedItem?.type === 'microskill';
    return (
      <div
        key={`microskill-${microskill.id}`}
        className={`flex items-center gap-2 py-1 px-2 cursor-pointer hover:bg-slate-800/50 transition-colors ${
          isSelected ? 'bg-blue-500/20 border-l-2 border-blue-500' : ''
        }`}
        style={{ paddingLeft: `${indent * 12}px` }}
        onClick={() => onSelectItem({ ...microskill, type: 'microskill' })}
      >
        <div className={`w-4 h-4 rounded-full border-2 flex items-center justify-center ${
          microskill.completed ? 'border-green-500 bg-green-500/20' : 'border-slate-600'
        }`}>
          {microskill.completed && <span className="text-green-400 text-xs">✓</span>}
        </div>
        <span className="text-xs text-slate-300 flex-1">{microskill.name}</span>
        <span className={`text-xs ${getProgressColor(microskill.progressPercentage)}`}>
          {Math.round(microskill.progressPercentage)}%
        </span>
      </div>
    );
  };

  const renderSkill = (skill: SkillProgress, indent: number) => {
    const isExpanded = expandedSkills.has(skill.id);
    const isSelected = selectedItem?.id === skill.id && selectedItem?.type === 'skill';
    const hasMicroskills = skill.microskills && skill.microskills.length > 0;

    return (
      <div key={`skill-${skill.id}`}>
        <div
          className={`flex items-center gap-2 py-1 px-2 cursor-pointer hover:bg-slate-800/50 transition-colors ${
            isSelected ? 'bg-blue-500/20 border-l-2 border-blue-500' : ''
          }`}
          style={{ paddingLeft: `${indent * 12}px` }}
        >
          {hasMicroskills && (
            <button
              onClick={(e) => {
                e.stopPropagation();
                toggleSkill(skill.id);
              }}
              className="text-slate-400 hover:text-blue-400 transition-colors"
            >
              {isExpanded ? '▼' : '▶'}
            </button>
          )}
          <div
            className="flex items-center gap-2 flex-1"
            onClick={() => onSelectItem({ ...skill, type: 'skill' })}
          >
            <div className={`w-4 h-4 rounded-full border-2 flex items-center justify-center ${
              skill.completed ? 'border-green-500 bg-green-500/20' : 'border-slate-600'
            }`}>
              {skill.completed && <span className="text-green-400 text-xs">✓</span>}
            </div>
            <span className="text-sm text-slate-200 flex-1">{skill.name}</span>
            <span className={`text-xs ${getProgressColor(skill.progressPercentage)}`}>
              {Math.round(skill.progressPercentage)}%
            </span>
          </div>
        </div>
        {isExpanded && hasMicroskills && (
          <div>
            {skill.microskills.map(microskill => renderMicroskill(microskill, indent + 1))}
          </div>
        )}
      </div>
    );
  };

  const renderSubcategory = (subcategory: SubcategoryProgress, indent: number) => {
    const isExpanded = expandedSubcategories.has(subcategory.id);
    const isSelected = selectedItem?.id === subcategory.id && selectedItem?.type === 'subcategory';
    const hasSkills = subcategory.skills && subcategory.skills.length > 0;

    return (
      <div key={`subcategory-${subcategory.id}`}>
        <div
          className={`flex items-center gap-2 py-1.5 px-2 cursor-pointer hover:bg-slate-800/50 transition-colors ${
            isSelected ? 'bg-blue-500/20 border-l-2 border-blue-500' : ''
          }`}
          style={{ paddingLeft: `${indent * 12}px` }}
        >
          {hasSkills && (
            <button
              onClick={(e) => {
                e.stopPropagation();
                toggleSubcategory(subcategory.id);
              }}
              className="text-slate-400 hover:text-blue-400 transition-colors"
            >
              {isExpanded ? '▼' : '▶'}
            </button>
          )}
          <div
            className="flex items-center gap-2 flex-1"
            onClick={() => onSelectItem({ ...subcategory, type: 'subcategory' })}
          >
            <div className={`w-4 h-4 rounded border-2 flex items-center justify-center ${
              subcategory.completed ? 'border-green-500 bg-green-500/20' : 'border-slate-600'
            }`}>
              {subcategory.completed && <span className="text-green-400 text-xs">✓</span>}
            </div>
            <span className="text-sm text-slate-200 flex-1 font-medium">{subcategory.name}</span>
            <span className={`text-xs ${getProgressColor(subcategory.progressPercentage)}`}>
              {Math.round(subcategory.progressPercentage)}%
            </span>
          </div>
        </div>
        {isExpanded && hasSkills && (
          <div>
            {subcategory.skills.map(skill => renderSkill(skill, indent + 1))}
          </div>
        )}
      </div>
    );
  };

  const renderCategory = (category: CategoryProgress, indent: number) => {
    const isExpanded = expandedCategories.has(category.id);
    const isSelected = selectedItem?.id === category.id && selectedItem?.type === 'category';
    const hasSubcategories = category.subcategories && category.subcategories.length > 0;

    return (
      <div key={`category-${category.id}`}>
        <div
          className={`flex items-center gap-2 py-2 px-2 cursor-pointer hover:bg-slate-800/50 transition-colors ${
            isSelected ? 'bg-blue-500/20 border-l-2 border-blue-500' : ''
          }`}
          style={{ paddingLeft: `${indent * 12}px` }}
        >
          {hasSubcategories && (
            <button
              onClick={(e) => {
                e.stopPropagation();
                toggleCategory(category.id);
              }}
              className="text-slate-400 hover:text-purple-400 transition-colors"
            >
              {isExpanded ? '▼' : '▶'}
            </button>
          )}
          <div
            className="flex items-center gap-2 flex-1"
            onClick={() => onSelectItem({ ...category, type: 'category' })}
          >
            <div className={`w-5 h-5 rounded border-2 flex items-center justify-center ${
              category.completed ? 'border-purple-500 bg-purple-500/20' : 'border-slate-600'
            }`}>
              {category.completed && <span className="text-purple-400 text-sm">✓</span>}
            </div>
            <span className="text-base text-slate-100 flex-1 font-semibold">{category.name}</span>
            <span className={`text-sm ${getProgressColor(category.progressPercentage)}`}>
              {Math.round(category.progressPercentage)}%
            </span>
          </div>
        </div>
        {isExpanded && hasSubcategories && (
          <div>
            {category.subcategories.map(subcategory => renderSubcategory(subcategory, indent + 1))}
          </div>
        )}
      </div>
    );
  };

  const renderDomain = (domain: DomainProgress) => {
    const isExpanded = expandedDomains.has(domain.id);
    const isSelected = selectedItem?.id === domain.id && selectedItem?.type === 'domain';
    const hasCategories = domain.categories && domain.categories.length > 0;

    return (
      <div key={`domain-${domain.id}`} className="mb-2">
        <div
          className={`flex items-center gap-2 py-2 px-3 cursor-pointer hover:bg-slate-800/70 transition-colors border-l-4 ${
            isSelected
              ? 'bg-blue-500/20 border-blue-500'
              : 'border-blue-500/30 hover:border-blue-500/50'
          }`}
        >
          {hasCategories && (
            <button
              onClick={(e) => {
                e.stopPropagation();
                toggleDomain(domain.id);
              }}
              className="text-slate-300 hover:text-blue-400 transition-colors font-bold"
            >
              {isExpanded ? '▼' : '▶'}
            </button>
          )}
          <div
            className="flex items-center gap-3 flex-1"
            onClick={() => onSelectItem({ ...domain, type: 'domain' })}
          >
            <div className={`w-6 h-6 rounded-lg border-2 flex items-center justify-center ${
              domain.completed ? 'border-cyan-500 bg-cyan-500/20' : 'border-slate-600'
            }`}>
              {domain.completed && <span className="text-cyan-400">✓</span>}
            </div>
            <span className="text-lg text-slate-50 flex-1 font-bold">{domain.name}</span>
            <span className={`text-sm font-semibold ${getProgressColor(domain.progressPercentage)}`}>
              {Math.round(domain.progressPercentage)}%
            </span>
          </div>
        </div>
        {isExpanded && hasCategories && (
          <div className="border-l border-slate-700 ml-6">
            {domain.categories.map(category => renderCategory(category, 1))}
          </div>
        )}
      </div>
    );
  };

  if (collapsed) {
    return (
      <aside className="w-16 bg-slate-900 border-r border-blue-500/30 flex flex-col items-center py-4">
        <button
          onClick={onToggleCollapse}
          className="p-2 text-slate-400 hover:text-blue-400 transition-colors"
        >
          ▶
        </button>
      </aside>
    );
  }

  return (
    <aside className="w-80 bg-slate-900 border-r border-blue-500/30 flex flex-col overflow-hidden">
      {/* Sidebar Header */}
      <div className="h-14 flex items-center justify-between px-4 border-b border-slate-700">
        <h2 className="text-lg font-bold text-blue-400">Skill Tree</h2>
        <button
          onClick={onToggleCollapse}
          className="p-1 text-slate-400 hover:text-blue-400 transition-colors"
        >
          ◀
        </button>
      </div>

      {/* Overall Progress */}
      {progressTree && (
        <div className="px-4 py-3 bg-slate-800/50 border-b border-slate-700">
          <div className="flex items-center justify-between mb-2">
            <span className="text-sm text-slate-400">Overall Progress</span>
            <span className={`text-sm font-bold ${getProgressColor(progressTree.overallProgress)}`}>
              {Math.round(progressTree.overallProgress)}%
            </span>
          </div>
          <div className="w-full h-2 bg-slate-700 rounded-full overflow-hidden">
            <div
              className="h-full bg-gradient-to-r from-blue-500 to-purple-500 transition-all duration-300"
              style={{ width: `${progressTree.overallProgress}%` }}
            />
          </div>
        </div>
      )}

      {/* Tree Content */}
      <div className="flex-1 overflow-y-auto py-2">
        {!progressTree ? (
          <div className="flex items-center justify-center h-full">
            <div className="text-slate-400">Loading...</div>
          </div>
        ) : progressTree.domains.length === 0 ? (
          <div className="flex items-center justify-center h-full">
            <div className="text-slate-400 text-center px-4">
              No domains found.<br />
              <span className="text-xs">Add domains to get started.</span>
            </div>
          </div>
        ) : (
          <div>
            {progressTree.domains.map(domain => renderDomain(domain))}
          </div>
        )}
      </div>
    </aside>
  );
}
