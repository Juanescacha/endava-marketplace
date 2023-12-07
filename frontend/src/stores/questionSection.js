import { defineStore } from "pinia";

export const useQuestionSection = defineStore("questionSection", {
	state: () => ({
		isUserTheSeller: false,
		listingId: 0,
		updateLatestQuestion: false,
	}),
	actions: {
		triggerUpdateOnLatestQuestion() {
			this.updateLatestQuestion = true;
			setTimeout(() => {
				this.updateLatestQuestion = false;
			}, 2000);
		},
	},
});
