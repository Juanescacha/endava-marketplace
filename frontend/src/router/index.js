import { createRouter, createWebHistory } from "vue-router";

import MainPage from "../views/MainPage.vue";
import Login from "../views/LoginPage.vue";
import ListingDetail from "../views/ListingDetail.vue";
import NewListing from "../views/NewListing.vue";
import UserDashboard from "../views/UserDashboard.vue";
import UserProfile from "../views/UserProfile.vue";
import SalesHistory from "../views/SalesHistory.vue";
import NotFoundPage from "../views/NotFoundPage.vue";
import { userIsLogedIn, saveUserInfoToStore } from "../utils/userSession";
import { useUserStore } from "../stores/user";

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
		path: "/listings/:id",
		component: ListingDetail,
		name: "Listing detail",
	},
	{
		path: "/listings/new",
		component: NewListing,
		name: "NewListing",
	},
	{
		path: "/users/me",
		component: UserDashboard,
		name: "User Dashboard",
		children: [
			{
				path: "profile",
				component: UserProfile,
				name: "Profile Section",
			},
			{
				path: "sales-history",
				component: SalesHistory,
				name: "Sales History",
			},
		],
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
	const isAuthenticated = userIsLogedIn();
	if (!isAuthenticated && to.name !== "Login") {
		return { name: "Login" };
	}
	if (to.name == "Login" && isAuthenticated) {
		return { name: "MainPage" };
	}

	const user = useUserStore();

	if (isAuthenticated && user.id === 0) saveUserInfoToStore();
});

export default router;
