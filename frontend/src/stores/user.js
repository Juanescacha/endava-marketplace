import { defineStore } from "pinia";

export const useUserStore = defineStore("user", {
	state: () => ({
		id: 0,
		name: "",
		email: "",
		image: "",
		isAdmin: false,
	}),
});
