import { createRouter, createWebHistory } from "vue-router";

import Login from "../views/LoginPage.vue";
import NewListing from "../views/NewListing.vue";

export default createRouter({
	history: createWebHistory(),
	routes: [
		{
			name: "Login",
			component: Login,
			path: "/login",
		},
		{
			name: "NewListing",
			component: NewListing,
			path: "/products/new",
		},
	],
});
