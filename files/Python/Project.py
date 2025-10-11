#Robotic Cell Management System
import threading
import time
from threading import Lock

lock = Lock()

#Data structures for robots, workers, and tasks
robots = {}
workers = {}
tasks = []
products = []


#This Function adds and removes robots and workers from the system.
#Could have used try and exceptions for error handling but chose this design because the code is clear, readable,
# and the errors occurring where predictable.
# Line 19-89
def add_new_robot(robot_id):
    #This code checks if the robot ID is valid by leaving it blank.
    #NB: .strip() ensures that if the user enters a string with only spaces, it will be treated as blank.
    if robot_id.strip() == "":
        print("Oops! Robot ID can't be blank. Please try again!")
        return

    #Add the robot if it doesn't already exist.
    if robot_id in robots:
        print(f"Robot '{robot_id}' is already part of the fleet. Try a different robot. ")
    else:
        robots[robot_id] = 'idle'
        print(f"Great! Robot '{robot_id}' has been added. Welcome to the team '{robot_id}'")


def remove_robot(robot_id):
    #Ensure the robot exists before attempting to remove it.
    if robot_id not in robots:
        print(f"Hmm... There's no robot with ID '{robot_id}'.")
        return

    #Check if the robot is idle.
    if robots[robot_id] == 'idle':
        print(f"Are you sure you want to remove '{robot_id}' from your troop? (Yes or No)  ")
        removal_choice = input("").strip()
        capitalized_removal_choice = removal_choice.capitalize()
        if capitalized_removal_choice == 'Yes':
            del robots[robot_id]
            print(f"Robot '{robot_id}' has been removed. Bye-bye! ")
        elif capitalized_removal_choice == 'No':
            print(f"That was a close call")
            return
        else:
            print("Invalid response. Please try again")


def hire_worker(worker_id):
    #Check if the worker ID is valid.
    #NB--LINE 21.
    if worker_id.strip() == "":
        print("Worker ID can't be empty. Please provide a valid ID.")
        return

    #Add the worker if they don't already exist.
    if worker_id in workers:
        print(f"Worker '{worker_id}' is already part of the team!")
    else:
        workers[worker_id] = 'idle'
        print(f"Welcome aboard! Worker '{worker_id}' has been added.")


def fire_worker(worker_id):
    #Ensure the worker exists before attempting to remove them.
    if worker_id not in workers:
        print(f"Looks like there's no worker with ID '{worker_id}'.")
        return

    #Check if the worker is idle before removing.
    if workers[worker_id] == 'idle':
        print(f"Are you sure you want to remove '{worker_id}' from your team? (Yes or No)  ")
        removal_choice = input("").strip()
        capitalized_removal_choice = removal_choice.capitalize()
        if capitalized_removal_choice == 'Yes':
            del workers[worker_id]
            print(f"Worker '{worker_id}' has been removed. Bye-bye! ")
            print(f"{worker_id} is now jobless")
        elif capitalized_removal_choice == 'No':
            print(f"That was a close call")
            return
        else:
            print("Invalid response. Please try again")


#This function assigns task to robots and workers by first filtering out idle ones.
def assign_task(task_type, required_robots, required_workers, duration):
    #Error handling for the inputs when assigning a task.
    #In this function I used try blocks because there were different errors occurring and less predictable.
    try:
        #Makes sure the task name is not empty.
        if not task_type.strip():
            raise ValueError("Task type cannot be empty. Please provide a valid task type.")

        #Sub try block to target an error when the input is not an integer.
        try:
            required_robots = int(required_robots)
            required_workers = int(required_workers)
            duration = int(duration)
        except ValueError:
            raise ValueError("Number of robots, worker, and duration must be an integer.")

        #This raises an error when the users input is not a positive integer.
        if required_robots <0:
            raise ValueError("Number of robots must be a positive integer")

        if required_workers < 0:
            raise ValueError("Number of workers must be a positive integer.")

        if duration <= 0:
            raise ValueError("Task duration must be a positive integer.")
    except ValueError as e:
        print(f"Warning!!: {e}")
        return


    #This filters out robots and workers that are currently idle.
    available_robots = []
    for robot, status in robots.items():
        if status == 'idle':
            available_robots.append(robot)

    available_workers = []
    for worker, status in workers.items():
        if status == 'idle':
            available_workers.append(worker)

    if len(available_robots) >= required_robots and len(available_workers) >= required_workers:
        #This code allocates resources for the task.
        assigned_robots = []
        for i in range(required_robots):
            assigned_robots.append(available_robots[i])

        assigned_workers = []
        for i in range(required_workers):
            assigned_workers.append(available_workers[i])

        #This code updates their statuses.
        for robot in assigned_robots:
            robots[robot] = 'working'
        for worker in assigned_workers:
            workers[worker] = 'working'

        #This code registers the task.
        task = {
            'type': task_type,
            'robots': assigned_robots,
            'workers': assigned_workers,
            'duration': duration,
            'status': 'in progress'
        }
        tasks.append(task)
        print(f"Task {task_type} assigned with duration {duration}.")
    else:
        print("Not enough resources available for this task.")


#This part of the code deals with the creation of a product.
#I also used try and exceptions in this part of the code because the error that occurred are less predictable.
def assign_product():
    try:
        #This part of the code asks for the name of the product.
        product_name = input("Enter product name: ").strip()
        if not product_name:
            raise ValueError("Product name cannot be empty.")

        #This also ask for the number of steps needed to assembly the product.
        stage_count = int(input("Enter the number of assembly steps: "))
        if stage_count <= 0:
            raise ValueError("The number of assembly steps must be greater than zero.")

        #This is the start of the steps needed to assemble the product.
        stages = []
        for i in range(stage_count):
            print(f"Stage {i + 1}:")
            #This variable asks for the type of task from the user and handles any value error.
            task_type = input("  Enter task type (e.g., welding, assembling, testing): ").strip()
            if not task_type:
                raise ValueError("Task type cannot be empty.")

            #This variable asks for the number of robots needed from the user and also handles any value error.
            try:
                required_robots = int(input("  Enter the number of robots required: "))
            except ValueError:
                raise ValueError("Number of robots must be an integer.")
            if required_robots < 0:
                raise ValueError("The number of robots required cannot be negative.")
            if required_robots > len([robot for robot, status in robots.items() if status == 'idle']):
                raise ValueError("Not enough idle robots available.")

            #This variable also asks for the number of workers needed and handles any error.
            try:
                required_workers = int(input("  Enter the number of workers required: "))
            except ValueError:
                raise ValueError("Number of workers must be an integer.")
            if required_workers < 0:
                raise ValueError("The number of workers required cannot be negative.")
            if required_workers > len([worker for worker, status in workers.items() if status == 'idle']):
                raise ValueError("Not enough idle workers available.")

            #This variable asks the duration of the in seconds and also handles any error.
            try:
                duration = int(input("  Enter the task duration (seconds): "))
            except ValueError:
                raise ValueError("Task duration must be an integer.")
            if duration <= 0:
                raise ValueError("Task duration must be greater than zero.")

            #This variable adds the validated step to the steps list.
            stages.append({
                'type': task_type,
                'robots': required_robots,
                'workers': required_workers,
                'duration': duration,
                'status': 'pending'
            })

        #This variable adds the product to the products list.
        product = {
            'name': product_name,
            'stages': stages,
            'current_stage': 0,
            'status': 'in progress'
        }
        products.append(product)
        print(f"Product {product_name} added to the queue for assembly.")
    #This handles any value error when the user inputs an invalid input and notifies the user with a message.
    except ValueError as e:
        print(f"Warning!!: {e}")


#This Function monitors the progress of tasks.
def update_task_progress():
    for task in tasks:
        if task['status'] == 'in progress':
            task['duration'] -= 1
            if task['duration'] <= 0:
                task['status'] = 'completed'
                for robot in task['robots']:
                    robots[robot] = 'idle'
                for worker in task['workers']:
                    workers[worker] = 'idle'
                print(f"\nTask {task['type']} completed.")


#This Function monitors the progress of products.
def update_product_progress():
    for product in products:
        if product['status'] == 'in progress':
            current_stage = product['current_stage']
            stage = product['stages'][current_stage]

            #If the current step is incomplete, assign the task.
            if stage['status'] == 'pending':
                assign_task(stage['type'], stage['robots'], stage['workers'], stage['duration'])
                stage['status'] = 'in progress'

            #Check if all tasks for this step are complete.
            all_tasks_completed = True
            for task in tasks:
                if task['type'] == stage['type'] and task['status'] != 'completed':
                    all_tasks_completed = False
                    break

            if all_tasks_completed:
                stage['status'] = 'completed'
                product['current_stage'] += 1

            #If all steps are complete.
            if product['current_stage'] >= len(product['stages']):
                product['status'] = 'completed'
                print(f"Product {product['name']} fully assembled!")


#This Function will monitor the statuses of the task and products.
def monitor_production():
    while True:
        with lock:
            update_task_progress()
            update_product_progress()
        time.sleep(1)


#This Function will display the status of all robots, workers, tasks and products.
def display_status():
    #This prints the status of all robots.
    print("\nRobots:")
    for robot, status in robots.items():
        print(f"  {robot}: {status}")

    #This prints the status of all workers.
    print("\nWorkers:")
    for worker, status in workers.items():
        print(f"  {worker}: {status}")

    #This prints the status of all Task.
    print("\nTasks:")
    for task in tasks:
        print(f"  {task['type']}: {task['status']} (Duration left: {task['duration']})")

    #This prints the status of all Products.
    print("\nProducts:")
    for product in products:
        print(
            f"  {product['name']}: {product['status']} (Current stage: {product['current_stage']}/{len(product['stages'])})")
    print()


#Main interactive loop.
def main_menu():
    #With this thread once you assign a task or products, it runs in the background therefore allowing you to choose a different option on the menu.
    #It prints a message once the task or product is complete.
    monitor_thread = threading.Thread(target=monitor_production, daemon=True)
    monitor_thread.start()

    #Since this is an infinite loop, the menu will be displayed continuously until the user choose to exit.
    while True:
        print("\n--- Welcome to the Robotic Cell Manager ---")
        print("Here are the options you have. Have fun")
        print("1. Add Robot")
        print("2. Remove Robot")
        print("3. Hire a Worker")
        print("4. Fire a Worker")
        print("5. Assign Task")
        print("6. Assign Product")
        print("7. Display Status")
        print("8. Exit")
        choice = input("Enter your choice: ")

        #This if statement runs when the user chooses 1 to add a robot.
        if choice == '1':
            robot_id = input("Enter Robot ID: ")
            add_new_robot(robot_id)
        #This elif statement runs when the user chooses 2 to remove a robot.
        elif choice == '2':
            robot_id = input("Enter Robot ID to you want to remove: ")
            remove_robot(robot_id)
        #This elif statement runs when the user chooses 3 to add a worker.
        elif choice == '3':
            worker_id = input("Enter Worker ID: ")
            hire_worker(worker_id)
        #This elif statement runs when the user chooses 4 to remove a worker.
        elif choice == '4':
            worker_id = input("Enter Worker ID to you want to remove: ")
            fire_worker(worker_id)
        #This elif statement runs when the user chooses 5 to add a task by asking for the type of task, number of robots and workers and duration.
        elif choice == '5':
            task_type = input("Enter Task Type (e.g., welding, assembling, testing): ")
            required_robots = (input("Enter Number of Robots Required: "))
            required_workers = (input("Enter Number of Workers Required: "))
            duration = (input("Enter Task Duration (seconds): "))
            assign_task(task_type, required_robots, required_workers, duration)
        #This elif statement runs when the user chooses 6 by calling the function 'assign_product'.
        elif choice == '6':
            assign_product()
        #This elif statement runs when the user chooses 7 by calling the function 'display_status'.
        elif choice == '7':
            display_status()
        #This elif statement runs when the user choose 8 to exit the menu and terminates the program.
        elif choice == '8':
            print("BYE...")
            break
        #This else statement runs as an error handling code which prints when an invalid choice is made.
        else:
            print("Invalid choice. Please try again.")

#Call the menu function to start the program.
main_menu()
