import { createRouter, createWebHistory } from "vue-router";

import MainPage from "../views/MainPage.vue";
import Login from "../views/LoginPage.vue";
import ListingDetail from "../views/ListingDetail.vue";
import NewListing from "../views/NewListing.vue";
import UserDashboard from "../views/UserDashboard.vue";
import UserProfile from "../views/UserProfile.vue";
import SalesHistory from "../views/SalesHistory.vue";
import PurchaseHistory from "@/views/PurchaseHistory.vue";
import PurchasedItem from "@/views/PurchasedItem.vue";
import NotFoundPage from "../views/NotFoundPage.vue";
import {
	userIsLogedIn,
	saveUserInfoToStore,
	saveUserInfoFromStoreToCookies,
	saveUserInfoFromCookiesToStore,
	userInfoIsInCookies,
} from "../utils/userSession";
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
			{
				path: "purchase-history",
				component: PurchaseHistory,
				name: "Purchase History",
			},
			{
				path: "purchase/:id",
				component: PurchasedItem,
				name: "Purchased Item",
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

router.beforeEach((to, from, next) => {
	const user = useUserStore();
	if (user.id === 0) {
		if (userInfoIsInCookies()) saveUserInfoFromCookiesToStore();
		else saveUserInfoToStore();
	}
	const isAuthenticated = userIsLogedIn();
	if (!isAuthenticated && to.name !== "Login") {
		next({ name: "Login" });
	} else if (to.name == "Login" && isAuthenticated) {
		next({ name: "MainPage" });
	} else {
		next();
	}
});

router.afterEach((to, from) => {
	addEventListener("beforeunload", () => {
		// This code is only executed on page reload
		saveUserInfoFromStoreToCookies();
	});
});

export default router;
