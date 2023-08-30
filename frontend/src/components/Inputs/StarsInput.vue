<script setup>
import { ref, reactive } from "vue";
import StarSvg from "./StarSVG.vue";

const emit = defineEmits(["ratingUpdated"]);
const props = defineProps({
	starCount: {
		type: Number,
		default: 5,
	},
	increment: {
		type: Number,
		default: 1,
	},
	color: {
		type: String,
	},
	label: {
		type: String,
		required: true,
	},
	dynamicClasses: {
		type: String,
	},
});

const rating = ref(0);
const valueOfEachStar = 1 / props.increment;
const starsFilling = reactive(Array(props.starCount).fill("empty"));

const updateFillings = ({ starId, mousePosition }) => {
	starsFilling.fill("full", 0, starId - 1);
	starsFilling[starId - 1] = mousePosition === "l" ? "half" : "full";
	starsFilling.fill("empty", starId);
};

const handleStarClick = payload => {
	const { starId, mousePosition } = payload;
	if (mousePosition === "r" || props.increment === 1) {
		rating.value = starId * valueOfEachStar;
	} else {
		rating.value = (starId - props.increment) * valueOfEachStar;
	}

	updateFillings(payload);
	emit("ratingUpdated", { rating: rating.value });
};

const handleStarHover = payload => {
	updateFillings(payload);
};

const determineStarFillingFromRating = () => {
	const stars = rating.value / valueOfEachStar;
	const aux = stars * 10;
	let starFilling;

	if (aux % 10 === 0) starFilling = "r";
	else starFilling = "l";

	return {
		starId: Math.ceil(stars),
		mousePosition: starFilling,
	};
};

const syncFillingsWithRatig = () => {
	const fillingInfo = determineStarFillingFromRating();
	updateFillings(fillingInfo);
};
</script>

<template>
	<div>
		<span>{{ label }}: {{ rating }}</span>
		<div
			class="flex"
			@mouseleave="syncFillingsWithRatig"
		>
			<star-svg
				v-for="star in starCount"
				:key="`star-${star}`"
				:star-id="star"
				:fill-amount="starsFilling[star - 1]"
				:color="color"
				@star-clicked="handleStarClick"
				@star-hover="handleStarHover"
			/>
		</div>
	</div>
</template>
