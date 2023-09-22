/** @type {import('tailwindcss').Config} */

const preline = require("preline/plugin");
const headlessui = require("@headlessui/tailwindcss");
const twforms = require("@tailwindcss/forms");
const typography = require("@tailwindcss/typography");

export default {
	content: [
		"index.html",
		"./src/**/*.{js,jsx,ts,tsx,vue,html}",
		"node_modules/preline/dist/*.js",
	],
	darkMode: "class",
	theme: {
		fontFamily: {
			sans: ["Roboto", "sans-serif"],
			roboto: ["Roboto", "sans-serif"],
			"roboto-condensed": ["Roboto Condensed", "sans-serif"],
		},
		extend: {
			colors: {
				endava: {
					50: "#fef5ee",
					100: "#fce8d8",
					200: "#f8ccb0",
					300: "#f4a87d",
					400: "#ee7b49",
					500: "#ea5925",
					600: "#de411b",
					700: "#b62f18",
					800: "#91271b",
					900: "#752219",
					950: "#3f0e0b",
				},
			},
		},
	},
	plugins: [twforms, preline, headlessui({ prefix: "ui" }), typography],
};
