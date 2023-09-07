import { createRouter, createWebHistory } from "vue-router";

import MainPage from "../views/MainPage.vue";
import Login from "../views/LoginPage.vue";
import NewListing from "../views/NewListing.vue";
import NotFoundPage from "../views/NotFoundPage.vue";
import { getCookie } from "../utils/cookies";

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
	{
		path: "/:catchAll(.*)",
		redirect: "/404",
	},
	{
		path: "/404",
		component: NotFoundPage,
		name: "404",
	},
];

const router = createRouter({
	history: createWebHistory(),
	routes,
});

router.beforeEach((to, from) => {
	const isAuthenticated = getCookie("access_token");
	if (!isAuthenticated && to.name !== "Login") {
		return { name: "Login" };
	}
	if (to.name == "Login" && isAuthenticated) {
		return { name: "MainPage" };
	}
});

export default router;
