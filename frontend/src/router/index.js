import { createRouter, createWebHistory } from "vue-router";

import NewListing from "../views/NewListing.vue";

export default createRouter({
	history: createWebHistory(),
	routes: [
		{
			name: "NewListing",
			component: NewListing,
			path: "/products/new",
		},
	],
});
