import speech_recognition as sr
import pyttsx3

recognizer = sr.Recognizer()

def capture_voice_input():
    with sr.Microphone() as source:
        print("Listening...")
        audio = recognizer.listen(source)
    return audio

def convert_voice_to_text(audio):
    try:
        text = recognizer.recognize_google(audio)
        print("You said: " + text)
    except sr.UnknownValueError:
        text = ""
        print("Sorry, I didn't understand that.")
    except sr.RequestError as e:
        text = ""
        print("Error; {0}".format(e))
    return text

def process_voice_command(text):
    if "hello" in text.lower():
        print("Hello! How can I help you?")
        return True
    elif "goodbye" in text.lower():
        print("Goodbye! Have a great day!")
        return False
    elif "learn" in text.lower():
        print("Learn opening")
        return text
    elif "notification" in text.lower():
        print("Notifications opening")
        return text
    else:
        print("I didn't understand that command. Please try again.")
    return True

def text_to_speech(text):
    engine = pyttsx3.init()
    engine.say(text)
    engine.runAndWait()
    return text

def main():
    end_program = True
    result_text = ""
    while end_program == True or end_program == result_text:
        audio = capture_voice_input()
        text = convert_voice_to_text(audio)
        end_program = process_voice_command(text)
        if end_program == True:
            text_to_speech("What can I help you")
        elif end_program == text:
            text_to_speech("I am opening" + text + "for you")
            return result_text
    if end_program == False:
        text_to_speech("Goodbye")
        return False

if __name__ == "__main__":
    main()