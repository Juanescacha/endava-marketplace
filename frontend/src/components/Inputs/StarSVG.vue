<script setup>
import fillings from "../../assets/starSVG";

const emit = defineEmits(["starClicked", "starHover"]);
const props = defineProps({
	size: {
		type: Number,
		default: 1,
	},
	color: {
		type: String,
		default: "#000000",
	},
	starId: {
		type: Number,
		required: true,
	},
	fillAmount: {
		type: String,
		default: "empty",
	},
});

const BASE_SIZE = 20;
const dimentions = BASE_SIZE * props.size;

const handleMouseMove = $ev => {
	const mouseX = $ev.offsetX;
	const mousePosition = mouseX < dimentions / 2 ? "l" : "r";

	emit("starHover", { starId: props.starId, mousePosition });
};

const handleStarClick = $ev => {
	const mouseX = $ev.offsetX;
	const mousePosition = mouseX < dimentions / 2 ? "l" : "r";

	emit("starClicked", { starId: props.starId, mousePosition });
};
</script>

<template>
	<svg
		:fill="color"
		:width="dimentions"
		:height="dimentions"
		viewBox="0 0 56 56"
		xmlns="http://www.w3.org/2000/svg"
		@mousemove="handleMouseMove"
		@click="handleStarClick"
	>
		<path :d="fillings[fillAmount]" />
	</svg>
</template>
