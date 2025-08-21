# ==== Base ====
FROM node:22-alpine AS base
WORKDIR /app
ENV NODE_ENV=production
# ускоряем npm
RUN npm config set update-notifier false

# ==== Dependencies ====
FROM base AS deps
# только package.json/package-lock.json — для кэширования слоёв
COPY package*.json ./
RUN npm ci --omit=dev

# ==== Runtime ====
FROM base AS runner
# создаём непривилегированного пользователя
RUN addgroup -S nodejs && adduser -S nodeuser -G nodejs
USER nodeuser

# копируем зависимости и исходники
COPY --from=deps /app/node_modules ./node_modules
COPY . .

# если порт задаётся через ENV в коде — можно не прописывать EXPOSE,
# но для удобства оставим:
EXPOSE 3000
ENV PORT=3000

# Healthcheck (опционально, если есть /health)
HEALTHCHECK --interval=30s --timeout=3s --start-period=10s CMD wget -qO- http://127.0.0.1:${PORT}/health || exit 1

CMD ["node", "index.js"]
