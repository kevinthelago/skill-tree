import type { NextConfig } from "next";

const nextConfig: NextConfig = {
  output: 'export',
  basePath: '/private',
  images: {
    unoptimized: true,
  },
};

export default nextConfig;
