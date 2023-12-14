import { defineStore } from "pinia";

export const useUserStore = defineStore("user", {
	state: () => ({
		id: 0,
		name: "",
		email: "",
		image: "https://upload.wikimedia.org/wikipedia/commons/thumb/b/b5/Windows_10_Default_Profile_Picture.svg/2048px-Windows_10_Default_Profile_Picture.svg.png",
		isAdmin: false,
	}),
});
