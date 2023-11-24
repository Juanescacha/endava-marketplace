import { defineConfig } from "vite";
import vue from "@vitejs/plugin-vue";
import { resolve } from "path";

export default defineConfig({
	plugins: [vue()],
	resolve: {
		alias: {
			"@": resolve("./src"),
		},
	},
	test: {
		environment: "jsdom",
		coverage: {
			reportsDirectory: "./tests/unit/coverage",
			exclude: [
				"**/router",
				"**/stores",
				"**/assets",
				"tests/helpers.js",
			],
		},
	},
});
