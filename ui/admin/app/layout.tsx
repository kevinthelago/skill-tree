import type { Metadata } from "next";
import "./globals.css";

export const metadata: Metadata = {
  title: "Skill Tree - Admin Dashboard",
  description: "Manage domains, skills, curricula, and resources",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="en">
      <body className="antialiased">
        {children}
      </body>
    </html>
  );
}
