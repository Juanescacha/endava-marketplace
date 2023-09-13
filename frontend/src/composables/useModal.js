import { ref } from "vue";

export default function useModal() {
	const isModalOpen = ref(false);

	const setModalOpen = value => {
		isModalOpen.value = value;
	};

	return { isModalOpen, setModalOpen };
}
