## About the project

This is a flashcard type app, similar to anki, meant to support Japanese learners.
This is a GUI focused app where users mainly interact with the app via clicks, and typing text, rather than typing commands.

### Value proposition

This app allows Japanese learners with English as their main language to create flashcards, each contain Japanese phrases and its corresponding English meaning/translation on the other side of the card. The app should support typing in English and Japanese, and be able to store up to 2000 (for now) cards.

### User Stories
1. As a learner, I can create cards, so that I can view them again later.
2. As a learner with English as the main language, I can input Japanese text on one side of the card, and English on the other, so that I can test myself on Japanese phrases.
3. As a learner, I can choose a random selection of cards, so that I can test myself from time to time
4. As a learner, I can edit cards, so that I can correct any typos


### Use Cases
UC01: Add a card
1. User chooses to add a card.
2. System opens an interface for the user to type in.
3. User types in the Japanese phrase and finishes.
4. System shows the other side of the card.
5. User types in the corresponding English translation.
6. User confirms creation.
7. A new card is created and is saved within the app.

UC02: Remove a card
1. User selects a card to remove.
2. System asks for confirmation.
3. User confirms.
4. Card is deleted.

UC03: Edit a card
1. User selects a card to edit.
2. System opens an interface with the text field prefilled with whatever is present currently.
3. User makes the necessary amendments.
4. User confirms the change.
5. System updates the card's contents and saves it.

UC04: Select a random card
1. User choose to select a random card.
2. System chooses a random card and displays the Japanese side
3. User clicks the card.
4. The card is flipped to the other side, revealing the English translation.
5. User clicks outside of the card's area.
6. System restores the interface to the state before the UC.


UC05: Select a group of random cards
1. User chooses to select a group of random cards.
2. System asks for number of cards, n.
3. User inputs the card count.
4. For n times, <u> select a random card (UC04) </u>, with appropriate transition animations between different cards.
5. User clicks outside of the card's area.
6. System restores the interface to the state before the UC.


UC06: Select and view a specific card
1. User scrolls through the card bank and clicks on a card.
2. System displays the Japanese side of the card.
3. User clicks the card.
4. System flips the card to the English card.
5. User clicks outside of the card's area.
6. System restores the interface to the state before the UC.
