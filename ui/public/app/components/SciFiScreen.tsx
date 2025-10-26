'use client';

import { useEffect, useRef, useState } from 'react';
import './SciFiScreen.css';

interface SciFiScreenProps {
  title: string;
  screenStack?: number;
  children: React.ReactNode;
}

const Screen = (i: number, title: string, children: React.ReactNode) => (
  <div key={'sci-fi-screen-' + i} className='sci-fi-screen' style={{ '--index': i } as React.CSSProperties}>
    <div className='sci-fi-screen-internal'>
      <div className='sci-fi-screen-image'></div>
      <div className="sci-fi-screen-content">
        <div className='sci-fi-screen-title-wrapper' style={{ '--stacks': '3' } as React.CSSProperties}>
          <svg viewBox='0 0 64 64'>
            <path d="M0 0 L16 16 L16 48 L0 64" />
          </svg>
          <span data-value={title} className='sci-fi-screen-title' style={{ '--index': '0' } as React.CSSProperties}>{title}</span>
          <span data-value={title} className='sci-fi-screen-title' style={{ '--index': '1' } as React.CSSProperties}>{title}</span>
          <span data-value={title} className='sci-fi-screen-title' style={{ '--index': '2' } as React.CSSProperties}>{title}</span>
          <svg viewBox='0 0 64 64'>
            <path d="M64 0 L48 16 L48 48 L64 64" />
          </svg>
        </div>
        {children}
      </div>
      <div className='sci-fi-screen-overlay'></div>
    </div>
  </div>
);

export default function SciFiScreen({ title, screenStack = 5, children }: SciFiScreenProps) {
  const wrapperRef = useRef<HTMLDivElement>(null);
  const letters = "abcdefghijklmnopqrstuvwxyz";

  useEffect(() => {
    // This effect is just for initial setup
  }, []);

  const handleMouseEnter = () => {
    const spans = wrapperRef.current?.querySelectorAll(".sci-fi-screen-title");
    if (!spans || spans.length === 0) return;

    let interval: NodeJS.Timeout | null = null;
    let iteration = 0;

    clearInterval(interval!);

    interval = setInterval(() => {
      const firstSpan = spans[0] as HTMLSpanElement;
      const dataValue = firstSpan.dataset.value || '';

      const innerText = dataValue
        .split("")
        .map((letter, index) => {
          if (index < iteration) {
            return dataValue[index];
          }
          return letters[Math.floor(Math.random() * 26)];
        })
        .join("");

      for (let i = 0; i < spans.length; i++) {
        (spans[i] as HTMLSpanElement).innerText = innerText;
      }

      if (iteration >= dataValue.length) {
        clearInterval(interval!);
      }

      iteration += 1 / 3;
    }, 30);
  };

  const screens = [];
  for (let i = 0; i < screenStack; i++) {
    screens.push(Screen(i, title, children));
  }

  return (
    <div
      ref={wrapperRef}
      className='sci-fi-screen-wrapper'
      style={{ '--stacks': screenStack } as React.CSSProperties}
      onMouseEnter={handleMouseEnter}
    >
      {screens.map(screen => screen)}
    </div>
  );
}
