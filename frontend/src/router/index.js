import { createRouter, createWebHistory } from "vue-router";

import MainPage from "@/views/MainPage.vue";
import Login from "@/views/LoginPage.vue";
import ListingDetail from "@/views/ListingDetail.vue";
import NewListing from "@/views/NewListing.vue";
import UserDashboard from "@/views/UserDashboard.vue";
import UserProfile from "@/views/UserProfile.vue";
import CategoriesManagement from "@/views/CategoriesManagement.vue";
import SalesHistory from "@/views/SalesHistory.vue";
import PurchaseHistory from "@/views/PurchaseHistory.vue";
import PurchasedItem from "@/views/PurchasedItem.vue";
import AdminPanel from "@/views/AdminPanel.vue";
import UserManagement from "@/views/UserManagement.vue";
import NotFoundPage from "@/views/NotFoundPage.vue";
import LogoutPage from "@/views/LogoutPage.vue";
import {
	userIsLogedIn,
	saveUserInfoFromServerToStore,
	saveUserInfoFromStoreToCookies,
	saveUserInfoFromCookiesToStore,
	userInfoIsInCookies,
	logoutUser,
} from "@/utils/userSession";
import { useUserStore } from "@/stores/user";

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
		path: "/logout",
		component: LogoutPage,
		name: "Logout",
		beforeEnter: (to, from) => {
			logoutUser();
		},
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
		path: "/admin-panel",
		component: AdminPanel,
		name: "Admin panel",
		children: [
			{
				path: "general",
				component: UserProfile,
				name: "General settings",
			},
			{
				path: "users",
				component: UserManagement,
				name: "Manage users",
			},
			{
				path: "categories",
				component: CategoriesManagement,
				name: "Manage categories",
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
		else saveUserInfoFromServerToStore();
	}
	const isAuthenticated = userIsLogedIn();
	if (!isAuthenticated && to.name !== "Login" && to.name !== "Logout") {
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

export { routes };

export default router;
