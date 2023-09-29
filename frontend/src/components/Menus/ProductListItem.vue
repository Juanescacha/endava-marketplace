<script setup></script>
<template>
	<div
		class="flex h-[max(4rem,auto)] items-center justify-between gap-3 px-6 py-3 lg:justify-around"
	>
		<div class="flex items-center gap-3">
			<slot name="left-side"></slot>
		</div>
		<div class="flex items-center gap-4">
			<slot name="right-side"></slot>
		</div>
	</div>
</template>
