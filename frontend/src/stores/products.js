// stores/counter.js
import { defineStore } from "pinia";

export const useProductStore = defineStore("productsList", {
	state: () => {
		return { productCards: [] };
	},

	actions: {
		update(value) {
			this.productCards = value;
		},
		add(value) {
			this.productCards = [...this.productCards, value];
		},
	},
});
