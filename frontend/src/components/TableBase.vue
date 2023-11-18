<script setup>
	defineProps({
		columns: {
			type: Array,
			required: true,
		},
	});
</script>

<template>
	<table class="mx-auto table-auto">
		<thead class="border-b-4 border-gray-900">
			<tr>
				<th
					v-for="column in columns"
					:key="column"
					class="px-4 py-2 text-center"
				>
					{{ column }}
				</th>
			</tr>
		</thead>
		<tbody>
			<slot></slot>
		</tbody>
	</table>
</template>
