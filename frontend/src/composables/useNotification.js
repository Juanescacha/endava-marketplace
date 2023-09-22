import { ref } from "vue";

export default function useNotification() {
	const showMsg = ref(null);
	const msgColor = ref("");

	const displayMsg = (msg, color) => {
		showMsg.value = msg;
		msgColor.value = color;
		setTimeout(() => {
			showMsg.value = null;
			msgColor.value = "";
		}, 5000);
	};

	return { showMsg, msgColor, displayMsg };
}
