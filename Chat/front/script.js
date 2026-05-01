async function enviar() {
        const input = document.getElementById("input");
        const messages = document.getElementById("messages");

        const pergunta = input.value;
        if (!pergunta) return;

        messages.innerHTML += `<div class="message user">${pergunta}</div>`;

        input.value = "";

        try {
            const response = await fetch("http://localhost:8080/chat", {
                method: "POST",
                headers: {
                    "Content-Type": "text/plain"
                },
                body: pergunta
            });

            const resposta = await response.text();

            messages.innerHTML += `<div class="message bot">${resposta}</div>`;

            messages.scrollTop = messages.scrollHeight;

        } catch (error) {
            messages.innerHTML += `<div class="message bot">Erro ao conectar com a IA</div>`;
        }
    }