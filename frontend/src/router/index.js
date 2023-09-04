import { createRouter, createWebHistory } from "vue-router";

import MainPage from "../views/MainPage.vue";
import Login from "../views/LoginPage.vue";
import NewListing from "../views/NewListing.vue";

const routes = [
	{
		path: "/",
		component: MainPage,
		name: "MainPage",
	},
	{
		path: "/login",
		component: Login,
		name: "Login",
	},
	{
		path: "/products/new",
		component: NewListing,
		name: "NewListing",
	},
];

export default createRouter({
	history: createWebHistory(),
	routes,
});
