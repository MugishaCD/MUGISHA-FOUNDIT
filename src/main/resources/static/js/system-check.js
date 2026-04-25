// system-check.js - Verifies API connectivity
		
(function() {
    console.log("FoundIt System Check: Initialized");
    
    // Basic check to ensure environment is ready
    window.addEventListener('load', () => {
        if (!window.api) {
            console.error("FoundIt Error: api.js not loaded!");
        } else {
            console.log("FoundIt Success: API module ready");
        }
    });
})();
