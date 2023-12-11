import { reactive } from "vue";

export default function useForms() {
	const formData = reactive({});

	const decorateFormData = fields => {
		if (!Array.isArray(fields)) {
			throw new Error("The parameter must be an array");
		}
		fields.forEach(field => {
			if (typeof field !== "string" || !isValidPropertyName(field)) {
				throw new Error("Invalid object property name");
			}
		});

		fields.forEach(field => {
			formData[field] = { value: "", valid: false };
		});
	};

	const isValidPropertyName = name => /^(?!\d)[\w$]+$/.test(name);

	const updateInputValue = (field, value) => {
		formData[field].value = value;
	};

	const validateTextInput = (field, value) => {
		let meetsMinimum = true,
			meetsMaximum = true;

		if (formData[field].hasOwnProperty("min")) {
			meetsMinimum = value.length >= formData[field].min;
		}
		if (formData[field].hasOwnProperty("max")) {
			meetsMaximum = value.length <= formData[field].max;
		}

		formData[field].valid = meetsMinimum && meetsMaximum;
		return formData[field].valid;
	};

	const validateNumericInput = (field, value) => {
		const num = Number(value);
		if (Number.isNaN(num)) {
			throw new Error("the value to validate must be numeric");
		}

		let meetsMinimum = true,
			meetsMaximum = true;

		if (formData[field].hasOwnProperty("min")) {
			meetsMinimum = num >= formData[field].min;
		}
		if (formData[field].hasOwnProperty("max")) {
			meetsMaximum = num <= formData[field].max;
		}

		formData[field].valid = meetsMinimum && meetsMaximum;
		return formData[field].valid;
	};

	const handleTextInput = $event => {
		updateInputValue($event.target.id, $event.target.value);
		validateTextInput($event.target.id, $event.target.value);
	};

	const handleNumericInput = $event => {
		const num = Number($event.target.value);
		if (Number.isNaN(num)) {
			return;
		}
		updateInputValue($event.target.id, num);
		validateNumericInput($event.target.id, num);
	};

	const handleMediaUpdate = media => {
		formData.media.value = media;
		if (formData.media.value.length > 0) {
			formData.media.valid = true;
		}
	};

	const handleSelectUpdate = $event => {
		const optionId =
			$event.target.options[$event.target.selectedIndex].dataset.id;

		updateInputValue($event.target.id, Number(optionId));
		formData[$event.target.id].valid = true;
	};

	const handleConditionUpdate = ({ rating }) => {
		if (rating > 0) {
			formData.condition.value = rating;
			formData.condition.valid = true;
		}
	};

	const removeNullsFromImages = images => {
		let auxMedia = images;
		if (images.includes(null)) {
			auxMedia = images.slice(0, images.indexOf(null));
		}

		return auxMedia;
	};

	const preventNegativeNumber = $event => {
		if ($event.key && $event.key === "-") {
			$event.preventDefault();
		}
	};

	const isValidForm = () => {
		const inputValues = Object.values(formData);
		const invalidInput = inputValues.find(input => !input.valid);

		return !invalidInput;
	};

	return {
		formData,
		decorateFormData,
		validateTextInput,
		validateNumericInput,
		handleTextInput,
		handleNumericInput,
		handleMediaUpdate,
		handleSelectUpdate,
		handleConditionUpdate,
		removeNullsFromImages,
		preventNegativeNumber,
		isValidForm,
	};
}
