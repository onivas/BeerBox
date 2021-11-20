# Beer app

L'app è stata fatta in Kotlin usando il pattern architetturale MVVM


Struttura dell'app:

- di            # Injection module
- domain        # Modelli di dominio
- repository    # Modelli network + REST client
- ui            # Activity/fragment e viewmodel
- utils         # Utils domain class, extensions..

Struttura dei test:

- repository    # Unit test del repository
- rule          # Test rule
- ui            # Tests unitari per la UI


Dipendenze usate:
- Hilt per la DI
- Okhttp3, Retrofit e moshi per il networking
- Glide per le immagini

Note:
1. Ho usato il navigation component, ma visti i flussi attuali dell'app, non è stato sfruttato per la navigazione, ma sicuramente rende l'app più propensa per sviluppi futuri di nuovi fragment/flussi
2. Ho gestito l'offline mode, in caso di errore, viene mostrato un messaggio di errore e un bottone di retry.
3. Ho seguito i mockup forniti, nella parte opzionale, non capendo da dove estrarre i tag, ho creato una lista di tag locali.


