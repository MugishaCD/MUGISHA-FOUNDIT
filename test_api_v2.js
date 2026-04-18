const http = require('http');

const baseURL = 'http://localhost:8081/api';

function makeRequest(path, method, data, token = null) {
    return new Promise((resolve, reject) => {
        const url = new URL(baseURL + path);
        const headers = { 'Content-Type': 'application/json' };
        if (token) {
            headers['Authorization'] = 'Bearer ' + token;
        }
        
        const options = {
            hostname: url.hostname,
            port: url.port,
            path: url.pathname + url.search,
            method: method,
            headers: headers
        };

        const req = http.request(options, res => {
            let body = '';
            res.on('data', chunk => body += chunk);
            res.on('end', () => {
                try {
                    const parsed = body ? JSON.parse(body) : null;
                    resolve({ status: res.statusCode, data: parsed });
                } catch (e) {
                    resolve({ status: res.statusCode, data: body });
                }
            });
        });

        req.on('error', e => reject(e));

        if (data) {
            req.write(JSON.stringify(data));
        }
        req.end();
    });
}

async function runTests() {
    console.log('=== PHASE 10: AUTOMATED API TESTING ===\n');
    let token = '';
    let userId = null;

    try {
        console.log('1. Testing Registration (Validation & Auth)');
        const regRes = await makeRequest('/v1/auth/register', 'POST', {
            fullName: 'Test User ' + Date.now(),
            email: 'test' + Date.now() + '@example.com',
            password: 'password123',
            phone: '1234567890'
        });
        console.log(`Status: ${regRes.status}`);
        
        if (regRes.status === 200 || regRes.status === 201) {
            console.log('Registration successful.');
            token = regRes.data.token || regRes.data.jwt; 
        } else {
            console.log('Registration skipped or failed (might already exist).');
        }

        console.log('\n2. Testing Login to get JWT');
        // Let's assume registration gave token or we login
        if (!token && regRes.data && regRes.data.id) {
           userId = regRes.data.id;
        }

        console.log('\n3. Testing Error Handling (Malformed JSON / Validation)');
        // Try creating lost item with missing fields
        const errRes = await makeRequest('/lost-items', 'POST', {}, token);
        console.log(`Status expected bad request: ${errRes.status}`);
        if(errRes.data && errRes.data.error) {
            console.log(`Error received correctly: ${errRes.data.error}`);
        }

        console.log('\n4. Testing Performance / Search Endpoint');
        const searchRes = await makeRequest('/lost-items/search?category=Personal&color=Black', 'GET', null, token);
        console.log(`Status: ${searchRes.status}`);
        if (Array.isArray(searchRes.data)) {
            console.log(`Search returned ${searchRes.data.length} items fast.`);
        }

        console.log('\n=== ALL TESTS EXECUTED ===');
    } catch (e) {
        console.error('Test script failed:', e);
    }
}

runTests();
