import Hapi from '@hapi/hapi';

const start = async () => {
    const server = Hapi.server({
        port: process.env.PORT || 3000,
        host: '0.0.0.0',
        routes: { cors: true }, // включить CORS для простоты
    });

    // Простой маршрут
    server.route({
        method: 'GET',
        path: '/',
        handler: () => ({ ok: true, message: 'Hello from Hapi!' }),
    });

    // Healthcheck
    server.route({
        method: 'GET',
        path: '/health',
        handler: () => 'OK',
    });

    await server.start();
    console.log(`✅ Server running at ${server.info.uri}`);

    // Грациозная остановка
    const shutdown = async (signal) => {
        console.log(`\n⏹  ${signal} received. Stopping...`);
        await server.stop({ timeout: 10_000 });
        console.log('👋 Stopped.');
        process.exit(0);
    };

    process.on('SIGINT', () => shutdown('SIGINT'));
    process.on('SIGTERM', () => shutdown('SIGTERM'));
};

start().catch((err) => {
    console.error(err);
    process.exit(1);
});
