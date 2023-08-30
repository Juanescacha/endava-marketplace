/** @type {import('tailwindcss').Config} */
export default {
	content: ["index.html", "./src/**/*.{js,jsx,ts,tsx,vue,html}"],
	theme: {
		fontFamily: {
			sans: ["Roboto", "sans-serif"],
			roboto: ["Roboto", "sans-serif"],
			"roboto-condensed": ["'Roboto Condensed'", "sans-serif"],
		},
		extend: {
			colors: {
				"endava-orange": "#de411b",
				"dark-orange": "#ad3315",
				"light-orange": "#f7481e",
				"endava-grey": "#48545b",
				"light-grey": "#9bb4be",
				"lighter-grey": "#f0f3f3",
			},
		},
	},
	plugins: [],
};
