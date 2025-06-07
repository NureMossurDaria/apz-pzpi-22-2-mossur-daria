from abc import ABC, abstractmethod

class Cat(ABC):
    @abstractmethod
    def speak(self):
        pass

class Dog(ABC):
    @abstractmethod
    def speak(self):
        pass

class FarmCat(Cat):
    def speak(self):
        return "Я сільський кіт"

class FarmDog(Dog):
    def speak(self):
        return "Я охороняю ферму"

class WildCat(Cat):
    def speak(self):
        return "Я дика пума"

class WildDog(Dog):
    def speak(self):
        return "Я вовк"


class AnimalFactory(ABC):
    @abstractmethod
    def create_cat(self) -> Cat:
        pass

    @abstractmethod
    def create_dog(self) -> Dog:
        pass


class FarmAnimalFactory(AnimalFactory):
    def create_cat(self):
        return FarmCat()

    def create_dog(self):
        return FarmDog()

class WildAnimalFactory(AnimalFactory):
    def create_cat(self):
        return WildCat()

    def create_dog(self):
        return WildDog()


def client_code(factory: AnimalFactory):
    cat = factory.create_cat()
    dog = factory.create_dog()
    
    print(cat.speak())
    print(dog.speak())

print("Тварини з ферми:")
client_code(FarmAnimalFactory())

print("\n Дикі тварини:")
client_code(WildAnimalFactory())
