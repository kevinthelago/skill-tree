# Sci-Fi Glitchy Login Page Design Reference

This document describes the sci-fi themed, glitch-effect login page design from the example React application. This design can serve as a reference for creating a similar aesthetic for the Skill Tree application's authentication interfaces.

## Overview

The login page features a **futuristic, cyberpunk-inspired design** with:
- Layered screen stacking effects
- RGB glitch animations
- Animated background imagery
- Smooth transitions and hover effects
- A retro-futuristic aesthetic with modern interactions

## Visual Design Concept

### Core Aesthetic
The design creates a "hacked terminal" or "retro-futuristic interface" feeling through:
- **Multi-layered screens**: 5 stacked screens creating depth
- **Glitch effects**: RGB color separation and horizontal shifting
- **Animated backgrounds**: Slowly panning space imagery with color filters
- **Scanline overlay**: Moving horizontal lines simulating CRT monitors
- **Neon accents**: Bright orange primary color (#C86400 / RGB 200, 100, 0)

### Dimensions
- **Width**: 320px
- **Height**: 520px (aspect ratio 320:520)
- **Border radius**: 1rem (rounded corners)
- **Box shadow**: 30px offset shadow in primary color

## Color Scheme

```css
:root {
  --background-color: #060608;      /* Nearly black background */
  --text-color: #FAFAFA;            /* Off-white text */
  --primary-color: 200 100 0;       /* Orange (#C86400) - used as RGB values */
  --secondary-color: 0 100 0;       /* Green (unused) */
  --image-hue-rotate: 20deg;        /* Background image color shift */
  --black: #060608;                 /* True black */
}
```

### Color Usage
- **Primary (Orange)**: Borders, shadows, overlay scanlines, button backgrounds
- **Background (Near-black)**: Main container backgrounds
- **Text (Off-white)**: All text and icons
- **Glitch colors**: Red, blue, green, hotpink for RGB separation effects

## Key Design Elements

### 1. Stacked Screen Effect

The login container consists of 5 layered screens:

```css
.sci-fi-screen {
    position: absolute;
    width: 304px;
    height: 494px;
    border-radius: 1rem;
    background-color: rgb(var(--primary-color));

    /* Stacking math */
    --stack-height: calc(100% / var(--stacks) - 1px);
    --inverse-index: calc(calc(var(--stacks) - 1) - var(--index));
    --clip-top: calc(var(--stack-height) * var(--index));
    --clip-bottom: calc(var(--stack-height) * var(--inverse-index));
    clip-path: inset(var(--clip-top) -10px var(--clip-bottom) -10px);
}
```

**Why 5 screens?**
- Creates visual depth and complexity
- Each screen enters with staggered animation (120ms delay between each)
- Odd and even screens have different glitch directions

### 2. Cutout Header/Footer

The top and bottom screens have polygonal cutouts:

**Top screen (first-child)**:
```css
clip-path: polygon(
    0 0,
    calc(50% - 64px) 0,           /* Left side of notch */
    calc(50% - 54px) 8px,         /* Inward slope */
    50% 8px,                       /* Center of notch */
    calc(50% + 54px) 8px,         /* Outward slope */
    calc(50% + 64px) 0,           /* Right side of notch */
    100% 0,
    100% calc(var(--stack-height) + 1px),
    0 calc(var(--stack-height) + 1px)
);
```

**Bottom screen (last-child)**: Mirror of the top notch at the bottom

### 3. Animated Background

The background features a panning space image:

```css
.sci-fi-screen-image {
    background-image: url('./4k-space-wallpaper-1.jpg');
    background-size: 300%;
    filter: sepia(100%) hue-rotate(var(--image-hue-rotate));
    opacity: 0.6;
    animation: pan-image 20s linear infinite;
}
```

**Pan animation**: Cycles through 5 different views of the image every 20 seconds:
- View 1 (0-20%): 37% 30% → 29% 47% at 400% zoom
- View 2 (20-40%): 60% 85% → 49% 81% at 500% zoom
- View 3 (40-60%): 80% 42% → 84% 33% at 300% zoom
- View 4 (60-80%): 0% 0% → 15% 4% at 300% zoom
- View 5 (80-100%): 80% 10% → 72% 14% at 300% zoom

### 4. Scanline Overlay

CRT-style moving scanlines:

```css
.sci-fi-screen-overlay {
    background-image: linear-gradient(
        rgb(var(--primary-color) / .15),
        rgb(var(--primary-color) / .15) 2px,
        transparent 2px,
        transparent 4px
    );
    background-size: 100% 4px;
    animation: pan-overlay 32s infinite linear;
}

@keyframes pan-overlay {
    from { background-position: 0% 0%; }
    to { background-position: 0% -100%; }
}
```

Creates horizontal lines that scroll vertically, completing a full cycle every 32 seconds.

## Animations & Effects

### Entry Animations

**Container entrance** (220ms cubic-bezier, staggered):
```css
@keyframes container-glitch-enter-1 {
    0% {
        opacity: 0;
        transform: translateX(-50%);
        box-shadow: -2px 3px 0 red, 2px -3px 0 blue;
    }
    60% {
        opacity: 0.5;
        transform: translateX(50%);
    }
    80% {
        transform: none;
        opacity: 1;
        box-shadow: 2px -3px 0 red, -2px 3px 0 blue;
    }
    100% {
        box-shadow: none;
    }
}
```

Odd screens use red/blue, even screens use green/hotpink.

**Title entrance** (340ms cubic-bezier, staggered):
- Same animation pattern as container
- Multiple stacked spans create layered glitch effect

### Continuous Glitch Effects

After a 2-second delay, screens and text continuously glitch:

```css
@keyframes container-glitch-1 {
    0% {
        box-shadow: -2px 3px 0 red, 2px -3px 0 blue;
        transform: translate(var(--glitch-translate));
    }
    1% {
        box-shadow: 2px -3px 0 red, -2px 3px 0 blue;
    }
    2%, 100% {
        box-shadow: none;
        transform: none;
    }
}
```

**Key details**:
- Glitch lasts only 2% of the 2-second loop (0.04 seconds)
- Creates brief, subtle RGB separation
- Alternates between odd (8px shift) and even (-8px shift) screens

### Title Scramble Effect

On mouse hover, the title letters scramble and resolve:

```javascript
handleMouseEnter() {
    let interval = setInterval(() => {
        // Replace each letter with random letter
        // Gradually reveal correct letters from left to right
        iteration += 1 / 3;
    }, 30);
}
```

Letters resolve at 1/3 character per 30ms interval.

## Component Structure

### Login Form Layout

```jsx
<div className="sci-fi-screen-content">
    {/* Title */}
    <div className="sci-fi-screen-title-wrapper">
        <svg>{/* Left bracket */}</svg>
        <span>Login</span>  {/* 3 stacked spans */}
        <svg>{/* Right bracket */}</svg>
    </div>

    {/* Email input */}
    <div className="sci-fi-screen-login-field-wrapper">
        <div className="sci-fi-screen-icon">{/* Email SVG */}</div>
        <input placeholder="email" />
    </div>

    {/* Password input */}
    <div className="sci-fi-screen-login-field-wrapper">
        <div className="sci-fi-screen-icon">{/* Lock SVG */}</div>
        <input placeholder="password" type="password" />
    </div>

    {/* Sign In button */}
    <div className="sci-fi-screen-login-button-wrapper">
        <button>Sign In</button>
    </div>

    {/* OAuth options */}
    <div className="sci-fi-screen-login-options">
        <a href={GITHUB_AUTH_URL}>
            <div className="sci-fi-screen-icon">{/* GitHub SVG */}</div>
            <div>Continue with GitHub</div>
        </a>
        <a href={GOOGLE_AUTH_URL}>
            <div className="sci-fi-screen-icon">{/* Google SVG */}</div>
            <div>Continue with Google</div>
        </a>
    </div>
</div>
```

## Interactive Elements

### Input Fields

```css
.sci-fi-screen-login-field-wrapper {
    display: flex;
    opacity: 0.7;
    border-bottom: 2px solid rgb(var(--primary-color) / 25%);
    transition: 0.2s;
}

.sci-fi-screen-login-field-wrapper:hover {
    opacity: 1;
}

.sci-fi-screen-login-field-input {
    color: var(--text-color);
    background-color: transparent;
    border: none;
    height: 38px;
    font-size: 0.8em;
}
```

**Features**:
- Transparent backgrounds
- Icon on the left (32x32px)
- Subtle bottom border in primary color (25% opacity)
- Opacity increases on hover (0.7 → 1.0)
- Placeholder text matches main text color

### Buttons

```css
.sci-fi-screen-login-button-wrapper {
    background-color: rgb(var(--primary-color) / 25%);
    opacity: 0.7;
    height: 40px;
    border-radius: 0.8rem;
    transition: 0.2s;
}

.sci-fi-screen-login-button-wrapper:hover {
    opacity: 1;
}
```

**Styling**:
- Semi-transparent orange background (25% opacity)
- Same hover opacity effect as inputs
- 2px letter-spacing for futuristic feel
- Matches container border-radius

### OAuth Options

Located at the bottom with absolute positioning:

```css
.sci-fi-screen-login-options {
    position: absolute;
    bottom: 12px;
    width: calc(100% - 22px);
}

.sci-fi-screen-login-option {
    display: flex;
    background-color: rgb(var(--primary-color) / 25%);
    opacity: 0.7;
    margin: 4px;
    border-radius: 0.8rem;
}
```

Each option shows an icon (Reddit/GitHub/Google) and text label.

## SVG Icons

All icons use FontAwesome SVG paths:
- **Email**: Envelope icon
- **Password**: Lock icon
- **GitHub**: GitHub logo
- **Google**: Google logo

Icons are styled with:
```css
.sci-fi-screen-icon {
    fill: var(--text-color);
    opacity: 0.5;
    height: 32px;
    width: 32px;
}
```

## CSS Variables Reference

The design uses CSS custom properties extensively:

### Stacking Variables
```css
--stacks: 5;                           /* Number of layered screens */
--index: 0-4;                          /* Current screen index */
--stack-height: calc(100% / var(--stacks) - 1px);
--inverse-index: calc(calc(var(--stacks) - 1) - var(--index));
--clip-top: calc(var(--stack-height) * var(--index));
--clip-bottom: calc(var(--stack-height) * var(--inverse-index));
```

### Animation Variables
```css
--glitch-translate: 8px;               /* Glitch shift amount (±8px) */
```

### Color Variables (RGB format for opacity)
```css
--primary-color: 200 100 0;            /* Used with rgb() function */
rgb(var(--primary-color));             /* Solid orange */
rgb(var(--primary-color) / 25%);       /* 25% opacity orange */
```

## Implementation Tips

### 1. **Layering Strategy**
Stack multiple identical elements with different animations to create depth. Use CSS `clip-path` to show only portions of each layer.

### 2. **Glitch Effect Recipe**
- Use RGB color-separated box-shadows (red, blue, green, hotpink)
- Brief horizontal translation (8px)
- Very short duration (2% of loop)
- Alternate directions on odd/even elements

### 3. **Entrance Timing**
```
Screen 1: 0ms
Screen 2: 120ms
Screen 3: 240ms
Screen 4: 360ms
Screen 5: 480ms
```

Stagger creates a cascading entrance effect.

### 4. **Background Image**
- Use high-resolution space/tech imagery
- Apply sepia filter then hue-rotate for color tinting
- Zoom to 300-500% and pan slowly
- Keep opacity at 60% to maintain readability

### 5. **Scanline Overlay**
- Create with repeating linear gradient
- 2px colored, 2px transparent
- Animate background-position vertically
- Use low opacity (15%) to avoid overwhelming

## Responsive Considerations

The current design is fixed-width (320px). For responsive implementation:

1. **Use viewport units** for container sizing:
   ```css
   width: min(320px, 90vw);
   ```

2. **Scale icons and text** proportionally:
   ```css
   font-size: clamp(0.8rem, 2vw, 1.5rem);
   ```

3. **Adjust shadow offset** on mobile:
   ```css
   box-shadow: min(30px, 5vw) min(30px, 5vw) 0 rgb(var(--primary-color));
   ```

## Accessibility Notes

Current design has low accessibility. To improve:

1. **Increase contrast ratios**
   - Text: #FAFAFA on #060608 = 19.4:1 ✓ (excellent)
   - Inputs: Need higher opacity or brighter borders

2. **Reduce motion** for users with preferences:
   ```css
   @media (prefers-reduced-motion: reduce) {
       .sci-fi-screen,
       .sci-fi-screen-title-wrapper span,
       .sci-fi-screen-image,
       .sci-fi-screen-overlay {
           animation: none !important;
       }
   }
   ```

3. **Add ARIA labels**:
   ```jsx
   <input
       placeholder="email"
       aria-label="Email address"
       type="email"
   />
   ```

4. **Keyboard navigation**:
   - Ensure all interactive elements are focusable
   - Add visible focus indicators
   - Test tab order

## Performance Considerations

1. **Use `will-change`** for animated elements:
   ```css
   .sci-fi-screen {
       will-change: transform, box-shadow;
   }
   ```

2. **Optimize animations** with `transform` and `opacity` only:
   - These properties trigger GPU acceleration
   - Avoid animating `width`, `height`, `left`, `right`

3. **Lazy load background image**:
   ```javascript
   const img = new Image();
   img.src = './4k-space-wallpaper-1.jpg';
   ```

4. **Consider `prefers-reduced-data`**:
   ```css
   @media (prefers-reduced-data: reduce) {
       .sci-fi-screen-image {
           background-image: none;
           background-color: rgb(var(--primary-color) / 5%);
       }
   }
   ```

## Adaptations for Skill Tree Application

### Color Customization
Replace the orange theme with Skill Tree brand colors:

```css
:root {
  --primary-color: /* Your brand color RGB */;
  --background-color: #060608;  /* Keep dark background */
  --text-color: #FAFAFA;        /* Keep high-contrast text */
}
```

### Background Theme
Replace space imagery with education/skill-related visuals:
- Neural network patterns
- Circuit board designs
- Abstract geometric patterns
- Tree/growth imagery (matching "skill tree" metaphor)

### Simplified Version
For a less dramatic effect:
- Reduce stacks to 2-3 layers
- Remove continuous glitch (keep only entrance animation)
- Use static background instead of animated
- Keep scanline overlay for retro feel

### Integration with Forms
The design accommodates:
- Email/password fields ✓
- OAuth providers (GitHub, Google) ✓
- Sign in/Sign up toggle (commented out in original)
- Error messages (add below inputs)
- "Remember me" checkbox (add below password)
- "Forgot password" link (add below sign in button)

## Summary

This sci-fi glitchy design creates a memorable, futuristic login experience through:

✨ **Visual Impact**
- Layered depth with 5 stacked screens
- RGB glitch effects on entrance and hover
- Animated space background with color filtering
- CRT-style scanline overlay

🎨 **Color & Typography**
- Dark background (#060608) with bright orange accents
- High-contrast white text (#FAFAFA)
- 0.8em - 3rem font sizes
- Letter-spacing for futuristic feel

⚡ **Animations**
- Staggered entrance (120ms delays)
- Brief glitch loops (2% of 2s cycle)
- Title scramble on hover (30ms intervals)
- 20s background pan cycle
- 32s scanline scroll cycle

🎯 **Interactive Elements**
- Opacity transitions on hover (0.7 → 1.0)
- Semi-transparent backgrounds (25% opacity)
- Clear visual feedback
- Consistent 0.2s transitions

This design can serve as inspiration for creating a unique, memorable authentication experience for the Skill Tree application while maintaining usability and accessibility standards.
